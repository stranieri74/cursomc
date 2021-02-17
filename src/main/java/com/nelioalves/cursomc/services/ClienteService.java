package com.nelioalves.cursomc.services;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.nelioalves.cursomc.domain.Cidade;
import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.domain.Endereco;
import com.nelioalves.cursomc.domain.enums.Perfil;
import com.nelioalves.cursomc.domain.enums.TipoCliente;
import com.nelioalves.cursomc.dto.ClienteDTO;
import com.nelioalves.cursomc.dto.ClienteNewDTO;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.repositories.EnderecoRepository;
import com.nelioalves.cursomc.security.UserSS;
import com.nelioalves.cursomc.services.exception.AuthorizationException;
import com.nelioalves.cursomc.services.exception.DateIntegrityException;
import com.nelioalves.cursomc.services.exception.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired //automaticamente é instanciada
   private ClienteRepository repo;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private EnderecoRepository enderecoRepo;
	
	@Autowired
	private S3Service s3Service;
	
	@Autowired
	private ClienteService clienteService;
	
   public Cliente find(Integer id) {
	   
	   UserSS user = UserService.authenticated();
	   if (user == null || !user.hasRole(Perfil.ADMIN)
		   && !id.equals(user.getId())) {
		   throw new AuthorizationException("Acesso Negado");
	   }
	   Optional<Cliente> obj = repo.findById(id);
	   return obj.orElseThrow(() -> new ObjectNotFoundException(
			   "Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName())); 
   }
   

   @Transactional
   public Cliente insert(Cliente obj) {
	   obj.setId(null);
	   obj = repo.save(obj);
	   enderecoRepo.saveAll(obj.getEnderecos());
	   return obj;
   }
   
   public Cliente update(Cliente obj) {
	   Cliente newObj = find(obj.getId());
	   //atualiza de acordo com as informações do bd
	   updateData(newObj, obj);
	   return repo.save(newObj);
   }
   
   public void delete(Integer id) {
	   find(id);
	   try {
	      repo.deleteById(id);
	   }catch(DataIntegrityViolationException e) {
		   throw new DateIntegrityException("Não é Possível excluir porque a entidades relacionadas");   
	   }
   }
   
   public List<Cliente> findAll(){
	   return repo.findAll();   
   }
   
   public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
	   PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction),
			   orderBy); 
	   
	   return repo.findAll(pageRequest);
   }
   
   //metodo auxiliar Categoria para CategoriaDTO
   
   public Cliente fromDTO(ClienteDTO objDto) {
	   return new Cliente(objDto.getId(),
			   objDto.getNome(),
			   objDto.getEmail(),
			   null,
			   null, 
			   null);
   }
   
   public Cliente fromDTO(ClienteNewDTO objDto) {
	    Cliente cli = new Cliente(null,
			   objDto.getNome(),
			   objDto.getEmail(),
			   objDto.getCpfOucnpj(),
			   TipoCliente.toEnum(objDto.getTipo()),
			   pe.encode(objDto.getSenha()));
	    Cidade cid = new Cidade(objDto.getCidadeId(),null, null);
	    
	    Endereco end = new Endereco(null,
	    		                 objDto.getLogradouro(),objDto.getNumero(),
	    		                 objDto.getComplemento(), objDto.getBairro(),
	    		                 objDto.getCep(), cli,
	    		                 cid
	    		                 );
	    
	    cli.getEnderecos().add(end);
	    cli.getTelefones().add(objDto.getTelefone1());
	    
	    if(objDto.getTelefone2() != null) {
	    	cli.getTelefones().add(objDto.getTelefone2());
	    }
	    
	    if(objDto.getTelefone3() != null) {
	    	cli.getTelefones().add(objDto.getTelefone3());
	    }
	    
	    return cli;
   }
   
   private void updateData(Cliente newObj, Cliente Obj) {
	   newObj.setNome(Obj.getNome());
	   newObj.setEmail(Obj.getEmail());
	   
   }
   
//metodo para enviar a foto
   public URI uploadProfilePicture(MultipartFile multipartFile) {
	  //verifica usuario logado
	   UserSS user = UserService.authenticated();
	   if(user==null) {
		   throw new AuthorizationException("Acesso Negado");
	   }
	   URI uri = s3Service.uploadFile(multipartFile);
	   Cliente cli = clienteService.find(user.getId());
	   cli.setImagemUrl(uri.toString());
	   repo.save(cli);
	   return uri;
	   
   }
}
