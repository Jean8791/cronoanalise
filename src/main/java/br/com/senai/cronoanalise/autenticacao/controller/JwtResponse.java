package br.com.senai.cronoanalise.autenticacao.controller;

import java.io.Serializable;

public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;
    private final JwtAuthenticationController.RetornoUsuarioLogado jwttoken;

    public JwtResponse(JwtAuthenticationController.RetornoUsuarioLogado jwttoken) {
        this.jwttoken = jwttoken;
    }

    public JwtAuthenticationController.RetornoUsuarioLogado getToken() {
        return this.jwttoken;
    }

}