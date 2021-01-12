package com.nelioalves.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nelioalves.cursomc.domain.ItemPedido;
import com.nelioalves.cursomc.domain.PagamentoComBoleto;
import com.nelioalves.cursomc.domain.Pedido;
import com.nelioalves.cursomc.domain.enums.EstadoPagamento;
import com.nelioalves.cursomc.repositories.ItemPedidoRepository;
import com.nelioalves.cursomc.repositories.PagamentoRepository;
import com.nelioalves.cursomc.repositories.PedidoRepository;
import com.nelioalves.cursomc.services.exception.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired //automaticamente é instanciada
   private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ClienteService clienteService;

	
   public Pedido find(Integer id) {
	   Optional<Pedido> obj = repo.findById(id);
	   return obj.orElseThrow(() -> new ObjectNotFoundException(
			   "Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName())); 
   }
   
   @Transactional
   public Pedido insert(Pedido obj) {
	   obj.setId(null);
	   obj.setInstante(new Date());
	   //para buscar o nome do cliente
	   obj.setCliente(clienteService.find(obj.getCliente().getId()));
	   
	   obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
	   obj.getPagamento().setPedido(obj);
	   
	   if(obj.getPagamento() instanceof PagamentoComBoleto) {
		  PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
		  boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());	  
	   }
	   
	   obj = repo.save(obj);
	   pagamentoRepository.save(obj.getPagamento());
	   
	   for(ItemPedido ip : obj.getItems()) {
		  ip.setDesconto(0.0);
		  ip.setProduto(produtoService.find(ip.getProduto().getId()));
		  ip.setPreco(ip.getProduto().getPreco());
		  ip.setPedido(obj);
	   }
	   itemPedidoRepository.saveAll(obj.getItems());
	   System.out.println(obj);
	   return obj;
   }
}
