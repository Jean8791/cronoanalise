package br.com.senai.cronoanalise.auth;

import br.com.senai.cronoanalise.comum.CronoProperties;
import br.com.senai.cronoanalise.usuario.Usuario;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenService {

    static final String EMISSOR = "cronoanalise";

    private final JwtEncoder encoder;
    private final CronoProperties props;

    public TokenService(JwtEncoder encoder, CronoProperties props) {
        this.encoder = encoder;
        this.props = props;
    }

    public Token gerar(Usuario usuario) {
        Instant agora = Instant.now();
        Instant expira = agora.plus(props.jwt().validade());
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(EMISSOR)
                .subject(usuario.getId().toString())
                .issuedAt(agora)
                .expiresAt(expira)
                .claim("nome", usuario.getNome())
                .claim("perfil", usuario.getPerfil().name())
                .build();
        String valor = encoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims)).getTokenValue();
        return new Token(valor, expira);
    }

    public record Token(String valor, Instant expiraEm) {
    }
}
