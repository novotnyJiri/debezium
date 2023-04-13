package io.debezium.resource;

import io.agroal.api.AgroalDataSource;
import io.debezium.entity.TestModel;
import io.quarkus.agroal.DataSource;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.pgclient.PgPool;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

@Path("Tests")
public class TestResource {

    @Inject
    @DataSource("postgresql")
    AgroalDataSource second_client;

    @Inject
    @ConfigProperty(name = "myapp.schema.create", defaultValue = "true")
    boolean schemaCreate;

    void config(@Observes StartupEvent ev) {
        if (schemaCreate) {
            initdb();
        }
    }

    @GET
    @Path("/db2")
    public Response getv2(){
        ResultSet rs;
        List<TestModel> list = new LinkedList<>();
        try(Connection conn = second_client.getConnection();
            Statement stmt = conn.createStatement()) {
            rs = stmt.executeQuery("SELECT id, name FROM tests ORDER BY name ASC");
            while(rs.next()) {
                list.add(TestModel.from(rs));
            }
        } catch (SQLException ex) {
            System.out.println("Error when getting items from second db" + ex);
            return Response.noContent().build();
        }
        return Response.ok(list).build();
    }
    private void initdb() {
        try(Connection conn = second_client.getConnection();
            Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS tests");
            stmt.execute("CREATE TABLE tests (id SERIAL PRIMARY KEY, name TEXT NOT NULL)");
            stmt.execute("INSERT INTO tests (name) VALUES ('TestPerson2')");
            stmt.execute("INSERT INTO tests (name) VALUES ('TestAnimal2')");
            stmt.execute("INSERT INTO tests (name) VALUES ('TestObject2')");
        } catch (SQLException ex) {
            System.out.println("Error when initiating second db" + ex);
        }

    }
    public static Multi<TestModel> findAll(PgPool client, AgroalDataSource second_client) {
        return client.query("SELECT id, name FROM tests ORDER BY name ASC").execute()
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(TestModel::from);
    }
}
