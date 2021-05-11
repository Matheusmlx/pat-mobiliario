package br.com.azi.patrimoniomobiliario.domain.usecase.naturezadespesa.buscar.exception;

public class VinculoMaterialServicoException extends RuntimeException{

    public VinculoMaterialServicoException(){ super("Registro de material serviço não encontrado!"); }
}
