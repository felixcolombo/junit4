package br.ce.wcaquino.builders;

import br.ce.wcaquino.entidades.Usuario;

public class UsuarioBuilder {

    private Usuario usuario;

    //Privado para que não possa ser criada novas instâncias fora do Builder;
    private UsuarioBuilder() {}

    //Público para que possa ser acessado externamente sem a necessidade de uma nova instância;
    public static UsuarioBuilder usuario1() {
        UsuarioBuilder usuarioBuilder = new UsuarioBuilder();
        usuarioBuilder.usuario = new Usuario();
        usuarioBuilder.usuario.setNome("Usuario 1");

        return usuarioBuilder;
    }

    public Usuario agora() {
        return usuario;
    }

}
