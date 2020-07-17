/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.marciojunior.vuttr;

import com.google.firebase.database.DatabaseReference;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;

@Path("tools")
public class ToolsResource {

    @Context
    private UriInfo context;

    public ToolsResource() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Tool> getJson() throws FileNotFoundException, IOException {



        // Cria as ferramentas
        Tool t1 = new Tool();
        //t1.setId(3);
        t1.setTitle("teste5");

        Tool t2 = new Tool();
        //t2.setId(4);
        t2.setTitle("teste6");

        List<Tool> toolList = new ArrayList<>();

        toolList.add(t1);
        toolList.add(t2);
/*
        // Salva

        
        DatabaseReference toolsRef = 
                FirebaseManager.getDatabaseRef().child("tools");

        //Map<String, Tool> users = new HashMap<>();
        //users.put("alanisawesome", new User("June 23, 1912", "Alan Turing"));
        //users.put("gracehop", new User("December 9, 1906", "Grace Hopper"));

        toolsRef.setValueAsync(toolList);
        
        */
        
        return toolList;

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public String putJson(Tool tool) {
        
        DatabaseReference toolsRef = 
                FirebaseManager.getDatabaseRef().child("tools");
       
        toolsRef.push().setValueAsync(tool);
        
        return "Status: 201 Created";        
        
    }

}
