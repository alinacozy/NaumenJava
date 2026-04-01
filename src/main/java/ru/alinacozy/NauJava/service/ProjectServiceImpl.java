package ru.alinacozy.NauJava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import ru.alinacozy.NauJava.entity.Project;
import ru.alinacozy.NauJava.entity.RequiredFloss;
import ru.alinacozy.NauJava.repository.ProjectRepository;
import ru.alinacozy.NauJava.repository.RequiredFlossRepository;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService{
    private final RequiredFlossRepository requiredFlossRepository;
    private final ProjectRepository projectRepository;
    private final PlatformTransactionManager transactionManager;

    @Autowired
    public ProjectServiceImpl(RequiredFlossRepository requiredFlossRepository, ProjectRepository projectRepository,
                           PlatformTransactionManager transactionManager)
    {
        this.requiredFlossRepository = requiredFlossRepository;
        this.projectRepository = projectRepository;
        this.transactionManager = transactionManager;
    }

    @Override
    public void deleteProjectWithRequiredFlosses(Long projectId) {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try
        {
            // Проверяем, существует ли проект
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new IllegalArgumentException("Project with id " + projectId + " not found"));
            // удалить нитки, связанные с данным проектом
            List<RequiredFloss> requiredFlosses = requiredFlossRepository.findByProjectId(projectId);
            for (RequiredFloss requiredFloss : requiredFlosses)
            {
                requiredFlossRepository.delete(requiredFloss);
            }
            // удалить проект
            projectRepository.deleteById(projectId);
            // фиксация транзакции
            transactionManager.commit(status);
        }
        catch (DataAccessException ex)
        {
            // откатить транзакцию в случае ошибки
            transactionManager.rollback(status);
            throw ex;
        }
    }
}
