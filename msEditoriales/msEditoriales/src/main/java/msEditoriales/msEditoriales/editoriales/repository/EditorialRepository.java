package msEditoriales.msEditoriales.editoriales.repository;

import msEditoriales.msEditoriales.editoriales.model.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorialRepository extends JpaRepository<Editorial, Long> {
}