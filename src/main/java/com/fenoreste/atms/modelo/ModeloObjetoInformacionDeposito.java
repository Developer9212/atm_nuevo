package com.fenoreste.atms.modelo;

import lombok.Data;

@Data
public class ModeloObjetoInformacionDeposito {
  private Comprobante response;
  private Integer EstatusProceso;
  private String NumeroAutorizacion;
  private String Mensaje;
}
