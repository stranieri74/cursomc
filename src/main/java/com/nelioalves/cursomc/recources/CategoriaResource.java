package com.nelioalves.cursomc.recources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.dto.CategoriaDTO;
import com.nelioalves.cursomc.services.CategoriaService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value="/categorias") 
public class CategoriaResource {
	@Autowired
	private CategoriaService service;
	
	 @ApiOperation(value="Busca por id")
	 @RequestMapping(value="/{id}", method=RequestMethod.GET)
   public
   ResponseEntity<Categoria> find(@PathVariable Integer id) {
	// ResponseEntity<?> find(@PathVariable Integer id) { 
	Categoria obj = service.find(id);
	 return ResponseEntity.ok().body(obj);  
   }
	 //comando para autorizar apenas perfis de admin
	 @PreAuthorize("hasAnyRole('ADMIN')")
	 @ApiOperation(value="Inseri categoria")
     @RequestMapping(method=RequestMethod.POST)
	 public ResponseEntity<Void> insert(@Valid @RequestBody CategoriaDTO objDto){
	   Categoria obj = service.fromDTO(objDto);
    	 obj = service.insert(obj); 
	   URI uri = ServletUriComponentsBuilder.fromCurrentRequest().
			   path("/{id}").buildAndExpand(obj.getId()).toUri();
	   
	   return ResponseEntity.created(uri).build();
	 }
     
	 @PreAuthorize("hasAnyRole('ADMIN')")
	 @ApiOperation(value="Atualiza categorias")
       @RequestMapping(value="/{id}", method=RequestMethod.PUT)
     public ResponseEntity<Void> update(@Valid @RequestBody CategoriaDTO objDto, @PathVariable Integer id){
    	   Categoria obj = service.fromDTO(objDto);
    	   obj.setId(id);
    	 obj = service.update(obj);
    	 return ResponseEntity.noContent().build();
     }
     
	 @PreAuthorize("hasAnyRole('ADMIN')")
	 @ApiOperation(value="Apaga categorias")
  	 @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
     public
     ResponseEntity<Void> delete(@PathVariable Integer id) {
  	   service.delete(id);
  	   return ResponseEntity.noContent().build();
  	   
  	 }
  	 
	 @RequestMapping(method=RequestMethod.GET)
	 @ApiOperation(value="Retorna todas categorias")
	   public
	   ResponseEntity<List<CategoriaDTO>> findAll() {
		// ResponseEntity<?> find(@PathVariable Integer id) { 
		 List<Categoria> list = service.findAll();
		 List<CategoriaDTO> listDTO = list.stream().map(obj -> 
		 new CategoriaDTO(obj)).collect(Collectors.toList());
		 return ResponseEntity.ok().body(listDTO);  
	   }
	 
	 @RequestMapping(value="/page", method=RequestMethod.GET)
	   public
	   ResponseEntity<Page<CategoriaDTO>> findPage(
			   @RequestParam(value="page", defaultValue="0") Integer page, 
			   @RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage, 
			   @RequestParam(value="orderBy", defaultValue="nome") String orderBy, 
			   @RequestParam(value="direction", defaultValue="ASC") String direction) {
		 Page<Categoria> list = service.findPage(page, linesPerPage, orderBy, direction);
		 Page<CategoriaDTO> listDTO = list.map(obj -> 
		 new CategoriaDTO(obj));
		 return ResponseEntity.ok().body(listDTO);  
	   }	 
}
