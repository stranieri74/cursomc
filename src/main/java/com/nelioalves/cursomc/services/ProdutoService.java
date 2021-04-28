package com.nelioalves.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.domain.Pedido;
import com.nelioalves.cursomc.domain.Produto;
import com.nelioalves.cursomc.repositories.CategoriaRepository;
import com.nelioalves.cursomc.repositories.ProdutoRepository;
import com.nelioalves.cursomc.services.exception.ObjectNotFoundException;

@Service
public class ProdutoService {

	@Autowired // automaticamente é instanciada
	private ProdutoRepository repo;
	
	@Autowired
	private CategoriaRepository categoriaRepository;

	public Produto find(Integer id) {
		Optional<Produto> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}
	/*public Page<Produto> findPage(String nome, List<Integer> ids, Integer page, Integer linesPerPage,
			String orderBy, String direction){
		   PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction),
				   orderBy); 
		   List<Categoria> categorias = Categoriarepo.findAllById(ids);
		return repo.search(nome, categorias, pageRequest);
		
	}*/
	
	public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		List<Categoria> categorias = categoriaRepository.findAllById(ids);
		return repo.findDistinctByNomeContainingAndCategoriasIn(nome, categorias, pageRequest);	
	/*@Transactional
	   public Produto insert(Produto obj) {
		   obj.setId(null);
		   
		   obj = repo.save(obj);
		    
		   return obj;
	   }
	   
	
	   public Produto fromDTO(ProdutoDTO objDto) {
		   Produto prod = new Produto(objDto.getId(), objDto.getNome(), objDto.getPreco());
		   List<Categoria> cad =   Categoriarepo.findAllById(objDto.getCategoria());
		   for (Categoria c : cad) {
			   c.getProdutos().add(prod);
			   prod.getCategorias().add(c);
		   }
		   
		   return prod;
	   }*/
	}
}
