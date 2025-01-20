package br.com.teste.cast.repository;

import br.com.teste.cast.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    boolean existsByCpfCnpj(String cpfCnpj);

    @Query("SELECT p FROM Usuario p WHERE p.cpfCnpj = :cpfCnpj")
    Optional<Usuario> findByCpfCnpj(String cpfCnpj);

    Optional<Usuario> findByLogin(String login);
}

