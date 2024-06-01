import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.se14.dto.ProjectDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ObjectMapperTest {

    @Test
    public void parseList() throws JsonProcessingException {
        String json = "[{\"project_id\":1,\"title\":\"asf\",\"description\":\"sfd\"},{\"project_id\":2,\"title\":\"afsd\",\"description\":\"afsd\"}]";
        ObjectMapper mapper = new ObjectMapper();
        List<ProjectDTO> projects = mapper.readValue(json, new TypeReference<List<ProjectDTO>>() {});

        System.out.println(projects);
    }
}
