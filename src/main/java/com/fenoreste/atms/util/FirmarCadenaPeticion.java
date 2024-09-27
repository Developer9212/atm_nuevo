package com.fenoreste.atms.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.text.DecimalFormat;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fenoreste.atms.entity.Tabla;
import com.fenoreste.atms.entity.TablaPK;
import com.fenoreste.atms.modelo.ObjetoFirma;
import com.fenoreste.atms.modelo.TxDeposito;
import com.fenoreste.atms.service.ITablaService;
import com.github.cliftonlabs.json_simple.JsonObject;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FirmarCadenaPeticion {

	@Autowired
	private ITablaService tablasService;
 	
	public ObjetoFirma objetoFirma(TxDeposito peticionDeposito) {
		log.info("...........Formando testing 18/07/2023 objeto peticion para firma.........");
		ObjetoFirma objetoFirma = new ObjetoFirma();

		try {

			objetoFirma.setIdCajero(peticionDeposito.getIdCajero().trim());
			objetoFirma.setFechaHora(peticionDeposito.getFechaHora().trim());
			objetoFirma.setTipoTX(peticionDeposito.getTipoTX());
			objetoFirma.setReferencia(peticionDeposito.getReferencia());
			objetoFirma.setCuenta(peticionDeposito.getCuenta().trim());
			objetoFirma.setSecuencia(peticionDeposito.getSecuencia());
			objetoFirma.setMontoIngresado(peticionDeposito.getMontoIngresado());
			objetoFirma.setMontoDepositado(peticionDeposito.getMontoDeposito());
			objetoFirma.setMontoCambio(peticionDeposito.getMontoCambio());
			
			
			
			//Buscamos la tabla donde esta configurada la palabra secreta
			TablaPK tb_pk = new TablaPK("cajero_receptor","secret");
			Tabla tbSecret = tablasService.buscarPorId(tb_pk);
			
			if(tbSecret != null) {
				//Firmamos la palabra secreta
			    //String firmaSecret = sign(tbSecret.getDato1());
			    //objetoFirma.setSecret(firmaSecret);
				objetoFirma.setSecret(tbSecret.getDato1());
			    
			}
		} catch (Exception e) {
			log.error("...........Error al conseguir objeto para firmar con SSl.......");
		}
		return objetoFirma;
	}
	
	//Formacion de firma cifrada con certificado
	public String firma(ObjetoFirma objetoFirma) {
		log.info(".............Construyendo testing 18/07/2023 cadena a firmar.............");
		String firma = "";
		StringBuilder sB = new StringBuilder();
		try {
			 DecimalFormat df = new DecimalFormat("0.00");
			 sB.append(objetoFirma.getIdCajero()).append("|");
		     sB.append(objetoFirma.getFechaHora() == null ? "" : objetoFirma.getFechaHora()).append("|");
		     sB.append(objetoFirma.getTipoTX() == null ? "" : objetoFirma.getTipoTX()).append("|");
		     sB.append(objetoFirma.getReferencia() == null ? "" : objetoFirma.getReferencia()).append("|");
		     sB.append(objetoFirma.getCuenta() == null ? "" : objetoFirma.getCuenta()).append("|");
		     sB.append(objetoFirma.getSecuencia() == null ? "" : objetoFirma.getSecuencia()).append("|");
		     sB.append(objetoFirma.getMontoIngresado() == null ? "" : df.format(objetoFirma.getMontoIngresado())).append("|");// objetoFirma.getMontoIngresado()).append("|");
		     sB.append(objetoFirma.getMontoDepositado() == null ? "" : df.format(objetoFirma.getMontoDepositado())).append("|");
		     sB.append(objetoFirma.getMontoCambio() == null ? "" : df.format(objetoFirma.getMontoCambio())).append("|");
		     sB.append(objetoFirma.getSecret() == null ? "" : objetoFirma.getSecret());
		     
		     log.info("Firmando la cadena:"+sB.toString());
		     //idCajero|fechaHora|tipoTx|referencia|cuenta|secuencia|montoIngresado|montoDepositado|montoCambio||secret
		     String cadena = sB.toString();
		     log.info("Cadena a firmar:"+cadena);
		     firma = sign(cadena);		     
		} catch (Exception e) {
			log.error(".................Error al firmar peticion......."+e.getMessage());
		}
		return firma;
	}
	
	//Leer datos Jks
    public String sign(String cadena) throws Exception {
        String firmaCod;
        //Direccion de mi keystore local
        
        //Direccion de mi keystore local
        TablaPK pkCertificado = new TablaPK("cajero_receptor", "datos_certificado");
        Tabla  certificadoTabla = tablasService.buscarPorId(pkCertificado);
        String fileName = ruta() + certificadoTabla.getDato1()+".jks";
        String password = certificadoTabla.getDato3() ;// "1973.C00p.CSN" ;//Pruebas(fenoreste2023)
        String alias = certificadoTabla.getDato2();//"1";//Pruebas(fenoreste)
        
        try {
            String data = cadena;
            Signature firma = Signature.getInstance("SHA256withRSA");
            RSAPrivateKey llavePrivada = getCertified(fileName, password, alias);
            firma.initSign(llavePrivada);
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            firma.update(bytes, 0, bytes.length);
            Base64.Encoder encoder = Base64.getEncoder();
            firmaCod = encoder.encodeToString(firma.sign());
            ///System.out.println("Firma: " + firmaCod);
        } catch (InvalidKeyException | SignatureException | NoSuchAlgorithmException e) {
        	log.error(".................Error al obtener jks........"+e.getMessage());
            throw new Exception("Exceptions" + e.getMessage(), e.getCause());
        }
        return firmaCod;
    }

    private RSAPrivateKey getCertified(String keystoreFilename, String password, String alias) throws Exception {
        RSAPrivateKey privateKey;
        try {
            KeyStore keystore = KeyStore.getInstance("JKS");
            keystore.load(new FileInputStream(keystoreFilename), password.toCharArray());
            privateKey = (RSAPrivateKey) keystore.getKey(alias, password.toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException | IOException | CertificateException ex) {
            log.error(".........Error al obtener certificado.......");
            throw new Exception("Exceptions" + ex.getMessage(), ex.getCause());
        }
        return privateKey;
    }
    
    
  //Parao obtener la ruta del servidor
    public static String ruta() {
        String home = System.getProperty("user.home");
        String separador = System.getProperty("file.separator");
        String actualRuta = home + separador + "Certificados" + separador + "Ca" + separador;
        return actualRuta;
    }
    
    
}
