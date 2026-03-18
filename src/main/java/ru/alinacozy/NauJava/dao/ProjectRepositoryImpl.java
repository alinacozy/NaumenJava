package ru.alinacozy.NauJava.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alinacozy.NauJava.entity.Floss;
import ru.alinacozy.NauJava.entity.Project;
import ru.alinacozy.NauJava.entity.RequiredFloss;
import ru.alinacozy.NauJava.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import java.util.List;

@Repository
public class ProjectRepositoryImpl implements ProjectRepositoryCustom {

    private final EntityManager entityManager;

    @Autowired
    public ProjectRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Project> findByUserIdAndProjectNameContainingIgnoreCase(Long userId, String projectName) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Project> criteriaQuery = criteriaBuilder.createQuery(Project.class);
        Root<Project> projectRoot = criteriaQuery.from(Project.class);

        // Join с User
        Join<Project, User> userJoin = projectRoot.join("user", JoinType.INNER);

        // Создаем предикаты
        Predicate userIdPredicate = criteriaBuilder.equal(userJoin.get("id"), userId);
        Predicate projectNamePredicate = criteriaBuilder.like(
                criteriaBuilder.lower(projectRoot.get("projectName")),
                "%" + projectName.toLowerCase() + "%"
        );

        // Комбинируем условия через AND
        Predicate finalPredicate = criteriaBuilder.and(userIdPredicate, projectNamePredicate);

        criteriaQuery.select(projectRoot).where(finalPredicate);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<Project> findByRequiredFlossBrand(String brand) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Project> criteriaQuery = criteriaBuilder.createQuery(Project.class);
        Root<Project> projectRoot = criteriaQuery.from(Project.class);

        // Делаем JOIN с RequiredFloss и Floss
        Join<Project, RequiredFloss> requiredFlossJoin = projectRoot.join("requiredFlosses", JoinType.INNER);
        Join<RequiredFloss, Floss> flossJoin = requiredFlossJoin.join("floss", JoinType.INNER);

        // Условие brand = переданный бренд
        Predicate brandPredicate = criteriaBuilder.equal(flossJoin.get("brand"), brand);

        criteriaQuery.select(projectRoot).where(brandPredicate).distinct(true);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
