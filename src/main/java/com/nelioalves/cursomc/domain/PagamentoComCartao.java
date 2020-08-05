package com.nelioalves.cursomc.domain;

import java.io.Serializable;

import javax.persistence.Entity;

import com.nelioalves.cursomc.domain.enums.EstadoPagamento;
@Entity
public class PagamentoComCartao extends Pagamento implements Serializable{
	

	private static final long serialVersionUID = 1L;

	private Integer quantidadeParcelas;
	
	public PagamentoComCartao() {
		
	}

	public PagamentoComCartao(Integer id, EstadoPagamento estado, Pedido pedido, Integer quantidadeParcelas) {
		super(id, estado, pedido);
		this.quantidadeParcelas = quantidadeParcelas; 
	}

	public Integer getQuantidadeParcelas() {
		return quantidadeParcelas;
	}

	public void setQuantidadeParcelas(Integer quantidadeParcelas) {
		this.quantidadeParcelas = quantidadeParcelas;
	}	
	
}
