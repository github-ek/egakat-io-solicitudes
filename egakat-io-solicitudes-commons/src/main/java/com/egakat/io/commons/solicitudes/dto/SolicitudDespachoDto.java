package com.egakat.io.commons.solicitudes.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class SolicitudDespachoDto extends AbstractSolicitudDespachoDto<SolicitudDespachoLineaDto> {

}