package br.com.senai.cronoanalise.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(ContainersConfig.class)
class ApiIntegracaoTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper json;

    private String login(String usuario, String senha) throws Exception {
        String corpo = mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"" + usuario + "\",\"senha\":\"" + senha + "\"}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return json.readTree(corpo).get("token").asText();
    }

    private static MockHttpServletRequestBuilder com(MockHttpServletRequestBuilder req, String token) {
        return req.header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON);
    }

    private JsonNode ler(MockHttpServletRequestBuilder req) throws Exception {
        return json.readTree(mvc.perform(req).andReturn().getResponse().getContentAsString());
    }

    @Test
    void senhaErradaEhRecusadaSemDizerSeOUsuarioExiste() throws Exception {
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"admin\",\"senha\":\"errada\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.detail").value("Login ou senha inválidos."));
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"naoexiste\",\"senha\":\"qualquer\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.detail").value("Login ou senha inválidos."));
    }

    @Test
    void respostasDeUsuarioNuncaTrazemSenha() throws Exception {
        String admin = login("admin", "admin12345");
        JsonNode usuarios = ler(com(get("/api/usuarios"), admin));

        assertThat(usuarios.size()).isGreaterThan(0);
        usuarios.forEach(u -> assertThat(u.fieldNames()).toIterable()
                .noneMatch(campo -> campo.toLowerCase().contains("senha")));
    }

    @Test
    void alunoNaoGerenciaUsuariosNemCadastros() throws Exception {
        String aluno = login("visitante", "visitante123");

        mvc.perform(com(get("/api/usuarios"), aluno)).andExpect(status().isForbidden());
        mvc.perform(com(post("/api/empresas"), aluno).content("""
                {"cnpj":"11222333000181","nome":"X","turnoMinutos":480,"fatorVelocidade":1,"nivelConfianca":0.95}"""))
                .andExpect(status().isForbidden());
        mvc.perform(com(get("/api/empresas"), aluno)).andExpect(status().isOk());
    }

    @Test
    void requisicaoSemTokenOuComTokenInvalidoRecebe401() throws Exception {
        mvc.perform(get("/api/empresas")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/empresas").header("Authorization", "Bearer abc.def.ghi")).andExpect(status().isUnauthorized());
    }

    @Test
    void bloquearUsuarioInvalidaOTokenDeleNaHora() throws Exception {
        String admin = login("admin", "admin12345");
        String login = "prof" + UUID.randomUUID().toString().substring(0, 8);
        String id = ler(com(post("/api/usuarios"), admin).content(
                "{\"nome\":\"Professora\",\"login\":\"" + login + "\",\"senha\":\"prof12345\",\"perfil\":\"PROFESSOR\"}"))
                .get("id").asText();
        String professor = login(login, "prof12345");
        mvc.perform(com(get("/api/empresas"), professor)).andExpect(status().isOk());

        mvc.perform(com(post("/api/usuarios/" + id + "/bloquear"), admin)).andExpect(status().isOk());

        mvc.perform(com(get("/api/empresas"), professor)).andExpect(status().isUnauthorized());
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"" + login + "\",\"senha\":\"prof12345\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void usuarioDeDemonstracaoNaoPodeSerAlterado() throws Exception {
        String admin = login("admin", "admin12345");
        String id = null;
        for (JsonNode u : ler(com(get("/api/usuarios"), admin))) {
            if ("visitante".equals(u.get("login").asText())) id = u.get("id").asText();
        }
        mvc.perform(com(put("/api/usuarios/" + id), admin).content(
                        "{\"nome\":\"x\",\"login\":\"visitante\",\"senha\":\"trocada123\",\"perfil\":\"ALUNO\"}"))
                .andExpect(status().isForbidden());
        mvc.perform(com(post("/api/usuarios/" + id + "/bloquear"), admin)).andExpect(status().isForbidden());
    }

    @Test
    void cronoanaliseCompletaComCalculoEPermissaoDoAutor() throws Exception {
        String admin = login("admin", "admin12345");
        String aluno = login("visitante", "visitante123");

        String cnpj = "45723174000110";
        String empresaId = ler(com(post("/api/empresas"), admin).content("""
                {"cnpj":"%s","nome":"Teste","turnoMinutos":480,"fatorVelocidade":1.10,"nivelConfianca":0.95,
                 "paradas":[{"descricao":"Café","minutos":10},{"descricao":"Ginástica","minutos":20}]}""".formatted(cnpj)))
                .get("id").asText();
        String produtoId = ler(com(post("/api/produtos"), admin).content("""
                {"empresaId":"%s","codigo":"P1","nome":"Peça",
                 "operacoes":[{"nome":"Cortar","quantidadeOperadores":1},{"nome":"Montar","quantidadeOperadores":1}]}""".formatted(empresaId)))
                .get("id").asText();

        JsonNode criada = ler(com(post("/api/cronoanalises"), aluno).content("""
                {"produtoId":"%s","cronoanalista":"Aluno","erroRelativo":0.05,
                 "temposPorOperacao":[[10,12,9,11,8],[20,19,22,21,18]]}""".formatted(produtoId)));

        JsonNode r = criada.get("resultado");
        assertThat(r.get("tempoMedioCiclo").decimalValue()).isEqualByComparingTo("30");
        assertThat(r.get("ciclosNecessariosArredondado").asInt()).isEqualTo(12);
        assertThat(r.get("tempoPadrao").decimalValue()).isEqualByComparingTo("35.20");
        assertThat(r.get("pecasPorHora").decimalValue()).isEqualByComparingTo("102.27");
        assertThat(criada.get("podeEditar").asBoolean()).isTrue();

        String outroAluno = "aluno" + UUID.randomUUID().toString().substring(0, 8);
        mvc.perform(com(post("/api/usuarios"), admin).content(
                "{\"nome\":\"Outro\",\"login\":\"" + outroAluno + "\",\"senha\":\"aluno12345\",\"perfil\":\"ALUNO\"}"));
        String tokenOutro = login(outroAluno, "aluno12345");
        mvc.perform(com(delete("/api/cronoanalises/" + criada.get("id").asText()), tokenOutro)).andExpect(status().isForbidden());
        mvc.perform(com(delete("/api/cronoanalises/" + criada.get("id").asText()), aluno)).andExpect(status().isNoContent());
    }

    @Test
    void cnpjInvalidoEhRecusado() throws Exception {
        String admin = login("admin", "admin12345");
        mvc.perform(com(post("/api/empresas"), admin).content("""
                {"cnpj":"11111111111111","nome":"X","turnoMinutos":480,"fatorVelocidade":1,"nivelConfianca":0.95}"""))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.detail").value("CNPJ inválido."));
    }
}
