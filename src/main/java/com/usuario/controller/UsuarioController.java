package com.usuario.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.usuario.model.Usuario;
import com.usuario.repository.UsuarioRepository;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Usuario adicionar(@RequestBody Usuario usuario) {
		return usuarioRepository.save(usuario);
	}
	
	@GetMapping
	public List<Usuario> listarTodos() {
		return usuarioRepository.findAll();
	}
	
	@GetMapping(path="/{id}")
	public ResponseEntity<Optional<Usuario>> listarPorId(@PathVariable("id") Long idUsuario){
		Optional<Usuario> usuario;
		
		try {
			usuario = usuarioRepository.findById(idUsuario);
			
			if(usuario.isEmpty()) {
				return new ResponseEntity<Optional<Usuario>>(HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<Optional<Usuario>>(usuario, HttpStatus.OK);
		} catch (NoSuchElementException exception) {
			return new ResponseEntity<Optional<Usuario>>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping(value="/{id}")
	public ResponseEntity<Usuario> alterar(@PathVariable("id") Long idUsuario, @RequestBody Usuario novoUsuario) {
		return usuarioRepository.findById(idUsuario).map(usuario -> {
			usuario.setNome(novoUsuario.getNome());
			Usuario usuarioAtualizado = usuarioRepository.save(usuario);
			return ResponseEntity.ok().body(usuarioAtualizado);
		}).orElse(ResponseEntity.notFound().build());
		
	}
	
	@DeleteMapping(path="/{id}")
	public ResponseEntity<Optional<Usuario>> deletar(@PathVariable("id") Long idUsuario) {
		try {
			usuarioRepository.deleteById(idUsuario);
			return new ResponseEntity<Optional<Usuario>>(HttpStatus.OK);
		} catch (NoSuchElementException exception) {
			return new ResponseEntity<Optional<Usuario>>(HttpStatus.NOT_FOUND);
		}
	}

}
