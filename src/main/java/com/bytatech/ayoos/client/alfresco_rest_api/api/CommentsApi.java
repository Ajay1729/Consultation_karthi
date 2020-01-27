/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (3.0.0-SNAPSHOT).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package com.bytatech.ayoos.client.alfresco_rest_api.api;

import com.bytatech.ayoos.client.alfresco_rest_api.model.CommentBody;
import com.bytatech.ayoos.client.alfresco_rest_api.model.CommentEntry;
import com.bytatech.ayoos.client.alfresco_rest_api.model.CommentPaging;
import com.bytatech.ayoos.client.alfresco_rest_api.model.Error;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-04-17T09:22:18.872+05:30[Asia/Kolkata]")

@Api(value = "Comments", description = "the Comments API")
public interface CommentsApi {

    @ApiOperation(value = "Create a comment", nickname = "createComment", notes = "Creates a comment on node **nodeId**. You specify the comment in a JSON body like this:  ```JSON {   \"content\": \"This is a comment\" } ```  **Note:** You can create more than one comment by  specifying a list of comments in the JSON body like this:  ```JSON [   {     \"content\": \"This is a comment\"   },   {     \"content\": \"This is another comment\"   } ] ``` If you specify a list as input, then a paginated list rather than an entry is returned in the response body. For example:  ```JSON {   \"list\": {     \"pagination\": {       \"count\": 2,       \"hasMoreItems\": false,       \"totalItems\": 2,       \"skipCount\": 0,       \"maxItems\": 100     },     \"entries\": [       {         \"entry\": {           ...         }       },       {         \"entry\": {           ...         }       }     ]   } } ``` ", response = CommentEntry.class, authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={ "comments", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successful response", response = CommentEntry.class),
        @ApiResponse(code = 400, message = "Invalid parameter: **commentBodyCreate** is invalid "),
        @ApiResponse(code = 401, message = "Authentication failed"),
        @ApiResponse(code = 403, message = "User does not have permission to create a comment"),
        @ApiResponse(code = 404, message = "**nodeId** does not exist "),
        @ApiResponse(code = 405, message = "Cannot comment on a node of this type"),
        @ApiResponse(code = 409, message = "**nodeId** is locked and you are not the lock owner "),
        @ApiResponse(code = 200, message = "Unexpected error", response = Error.class) })
    @RequestMapping(value = "/nodes/{nodeId}/comments",
        produces = "application/json", 
        consumes = "application/json",
        method = RequestMethod.POST)
    ResponseEntity<CommentEntry> createComment(@ApiParam(value = "The identifier of a node.",required=true) @PathVariable("nodeId") String nodeId,@ApiParam(value = "The comment text. Note that you can also provide a list of comments." ,required=true )  @Valid @RequestBody CommentBody commentBody,@ApiParam(value = "A list of field names.  You can use this parameter to restrict the fields returned within a response if, for example, you want to save on overall bandwidth.  The list applies to a returned individual entity or entries within a collection.  If the API method also supports the **include** parameter, then the fields specified in the **include** parameter are returned in addition to those specified in the **fields** parameter. ") @Valid @RequestParam(value = "fields", required = false) List<String> fields);


    @ApiOperation(value = "Delete a comment", nickname = "deleteComment", notes = "Deletes the comment **commentId** from node **nodeId**.", authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={ "comments", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successful response"),
        @ApiResponse(code = 401, message = "Authentication failed"),
        @ApiResponse(code = 403, message = "User does not have permission to delete a comment"),
        @ApiResponse(code = 404, message = "**nodeId** or **commentId** does not exist "),
        @ApiResponse(code = 409, message = "**nodeId** is locked and you are not the lock owner "),
        @ApiResponse(code = 200, message = "Unexpected error", response = Error.class) })
    @RequestMapping(value = "/nodes/{nodeId}/comments/{commentId}",
        produces = "application/json", 
        method = RequestMethod.DELETE)
    ResponseEntity<Void> deleteComment(@ApiParam(value = "The identifier of a node.",required=true) @PathVariable("nodeId") String nodeId,@ApiParam(value = "The identifier of a comment.",required=true) @PathVariable("commentId") String commentId);


    @ApiOperation(value = "List comments", nickname = "listComments", notes = "Gets a list of comments for the node **nodeId**, sorted chronologically with the newest comment first.", response = CommentPaging.class, authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={ "comments", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = CommentPaging.class),
        @ApiResponse(code = 400, message = "Invalid parameter: **nodeId** exists but does not identify a file or a folder,  or the value of **maxItems** is invalid, or the value of **skipCount** is invalid "),
        @ApiResponse(code = 401, message = "Authentication failed"),
        @ApiResponse(code = 403, message = "User does not have permission read comments on the node"),
        @ApiResponse(code = 404, message = "**nodeId** does not exist "),
        @ApiResponse(code = 200, message = "Unexpected error", response = Error.class) })
    @RequestMapping(value = "/nodes/{nodeId}/comments",
        produces = "application/json", 
        method = RequestMethod.GET)
    ResponseEntity<CommentPaging> listComments(@ApiParam(value = "The identifier of a node.",required=true) @PathVariable("nodeId") String nodeId,@Min(0)@ApiParam(value = "The number of entities that exist in the collection before those included in this list.  If not supplied then the default value is 0. ", defaultValue = "0") @Valid @RequestParam(value = "skipCount", required = false, defaultValue="0") Integer skipCount,@Min(1)@ApiParam(value = "The maximum number of items to return in the list.  If not supplied then the default value is 100. ", defaultValue = "100") @Valid @RequestParam(value = "maxItems", required = false, defaultValue="100") Integer maxItems,@ApiParam(value = "A list of field names.  You can use this parameter to restrict the fields returned within a response if, for example, you want to save on overall bandwidth.  The list applies to a returned individual entity or entries within a collection.  If the API method also supports the **include** parameter, then the fields specified in the **include** parameter are returned in addition to those specified in the **fields** parameter. ") @Valid @RequestParam(value = "fields", required = false) List<String> fields);


    @ApiOperation(value = "Update a comment", nickname = "updateComment", notes = "Updates an existing comment **commentId** on node **nodeId**.", response = CommentEntry.class, authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={ "comments", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = CommentEntry.class),
        @ApiResponse(code = 400, message = "Invalid parameter: **commentBodyUpdate** is invalid "),
        @ApiResponse(code = 401, message = "Authentication failed"),
        @ApiResponse(code = 403, message = "User does not have permission to update a comment"),
        @ApiResponse(code = 404, message = "**nodeId** or **commentId** does not exist "),
        @ApiResponse(code = 409, message = "**nodeId** is locked and you are not the lock owner "),
        @ApiResponse(code = 200, message = "Unexpected error", response = Error.class) })
    @RequestMapping(value = "/nodes/{nodeId}/comments/{commentId}",
        produces = "application/json", 
        consumes = "application/json",
        method = RequestMethod.PUT)
    ResponseEntity<CommentEntry> updateComment(@ApiParam(value = "The identifier of a node.",required=true) @PathVariable("nodeId") String nodeId,@ApiParam(value = "The identifier of a comment.",required=true) @PathVariable("commentId") String commentId,@ApiParam(value = "The JSON representing the comment to be updated." ,required=true )  @Valid @RequestBody CommentBody commentBody,@ApiParam(value = "A list of field names.  You can use this parameter to restrict the fields returned within a response if, for example, you want to save on overall bandwidth.  The list applies to a returned individual entity or entries within a collection.  If the API method also supports the **include** parameter, then the fields specified in the **include** parameter are returned in addition to those specified in the **fields** parameter. ") @Valid @RequestParam(value = "fields", required = false) List<String> fields);

}
