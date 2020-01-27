package com.bytatech.ayoos.client.digitalsigning.api;

/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.List;

import javax.validation.Valid;

import org.alfresco.service.cmr.repository.NodeRef;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bytatech.ayoos.client.digitalsigning.model.Sign;
import com.bytatech.ayoos.client.digitalsigning.model.SigningCredentials;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * TODO Provide a detailed description here 
 * @author Karthikeyan P.K
 * karthikeyan, karthikeyan.p.k@lxisoft.com
 */


@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2018-07-30T20:52:32.970+05:30[Asia/Kolkata]")

@Api(value = "SigningCredentials", description = "the signing API")

public interface DigitalSigningApi {

	@ApiResponses(value = { 
	        @ApiResponse(code = 200, message = "Indicates the table exists and the table row data is returned", response = Sign.class),
	        @ApiResponse(code = 401, message = "Unauthorized"),
	        @ApiResponse(code = 403, message = "Forbidden"),
	        @ApiResponse(code = 404, message = "Indicates the requested table does not exist.") })
	    @RequestMapping(value = "api/digitalSigning/sign",
	        produces = "application/json", 
	        method = RequestMethod.POST)
	
	 ResponseEntity<SigningCredentials> signDocument(@ApiParam(value = "SigningCredentials" ,required=true ) @RequestBody SigningCredentials signingCredentials);
	
	
	 /*ResponseEntity<List<Sign>> getSigningResponse(
			 @ApiParam(value = "document",required=true) @RequestParam("document") List<NodeRef> document,
			 @ApiParam(value = "password",required=true ) @RequestParam("password") String password);
*/
}
