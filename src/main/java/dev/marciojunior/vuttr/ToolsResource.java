package dev.marciojunior.vuttr;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ejb.Asynchronous;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("tools")
public class ToolsResource {

    public ToolsResource() {
    }

    @GET
    @Asynchronous
    @Produces(MediaType.APPLICATION_JSON)
    public void getTools(@QueryParam("tag") final String tag, @Suspended final AsyncResponse asyncResponse) {

        DatabaseReference toolsRef
                = FirebaseManager.getDatabaseRef().child("tools");

        toolsRef.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {

            List<Tool> toolList = new ArrayList<>();
            Tool tool;
            boolean tagEncontrada;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    tool = ds.getValue(Tool.class);

                    // Filtro
                    if (tag != null) {

                        tagEncontrada = false;

                        for (String t : tool.tags) {

                            if (t.equalsIgnoreCase(tag)) {
                                tagEncontrada = true;
                            }

                        }

                        if (!tagEncontrada) {
                            continue;
                        }

                    }

                    toolList.add(tool);

                }

                // Ordem decrescente
                Collections.reverse(toolList);
                asyncResponse.resume(toolList);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Falha na consulta: "
                        + databaseError.getCode());
            }

        });

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Tool putTool(Tool tool, @Context final HttpServletResponse response) throws IOException {

        DatabaseReference toolsRef
                = FirebaseManager.getDatabaseRef().child("tools");

        // Cria um novo nó e recupera a chave
        DatabaseReference newToolRef = toolsRef.push();
        String key = newToolRef.getKey();

        // Seta a chave no usuário e salva
        tool.setId(key);
        newToolRef.setValueAsync(tool);

        // Configura o retorno
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.setContentType(MediaType.APPLICATION_JSON);
        response.flushBuffer();

        return tool;

    }

    @DELETE
    @Asynchronous
    @Path("{id}")
    public void deleteTool(@PathParam("id") String id, @Suspended final AsyncResponse asyncResponse) {

        DatabaseReference toolsRef
                = FirebaseManager.getDatabaseRef().child("tools");

        toolsRef.child(id).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError de, DatabaseReference dr) {
                Response response = Response.noContent().build();
                asyncResponse.resume(response);
            }
        });

    }

}
