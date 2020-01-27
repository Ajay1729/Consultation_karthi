package com.bytatech.ayoos.client.digitalsigning.api;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bytatech.ayoos.client.digitalsigning.model.Sign;
import com.bytatech.ayoos.client.digitalsigning.model.SigningCredentials;
import com.bytatech.ayoos.client.digitalsigning.model.Upload;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2018-07-30T20:52:32.970+05:30[Asia/Kolkata]")

@Api(value = "Upload", description = "the Upload sign and key API")

public interface DigitalSigningUploadApi {

	@ApiResponses(value = { 
	        @ApiResponse(code = 200, message = "Indicates the table exists and the table row data is returned", response = Sign.class),
	        @ApiResponse(code = 401, message = "Unauthorized"),
	        @ApiResponse(code = 403, message = "Forbidden"),
	        @ApiResponse(code = 404, message = "Indicates the requested table does not exist.") })
	    @RequestMapping(value = "api/digitalSigning/upload",
	        produces = "application/json", 
	        method = RequestMethod.POST)
	
	ResponseEntity<SigningCredentials> signDocument(@ApiParam(value = "Upload" ,required=true ) Upload upload);
}
