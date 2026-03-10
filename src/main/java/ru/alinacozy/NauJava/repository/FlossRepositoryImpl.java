package ru.alinacozy.NauJava.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.alinacozy.NauJava.entity.Floss;

import java.util.List;
import java.util.stream.IntStream;

@Component
public class FlossRepositoryImpl implements FlossRepository{
    private final List<Floss> flossContainer;

    @Autowired
    public FlossRepositoryImpl(List<Floss> flossContainer)
    {
        this.flossContainer = flossContainer;
    }


    @Override
    public void create(Floss entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Floss entity cannot be null");
        }
        if (entity.getId() == null) {
            entity.setId(generateNextId());
        }
        flossContainer.add(entity);
    }

    @Override
    public Floss read(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return flossContainer.stream()
                .filter(floss -> id.equals(floss.getId()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void update(Floss entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("Floss entity and its ID cannot be null for update");
        }

        int index = IntStream.range(0, flossContainer.size())
                .filter(i -> entity.getId().equals(flossContainer.get(i).getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Floss with id " + entity.getId() + " not found for update"));

        flossContainer.set(index, entity);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null for delete");
        }

        // Удаляем по id
        flossContainer.removeIf(floss -> id.equals(floss.getId()));
    }

    @Override
    public List<Floss> findAll() {
        return flossContainer;
    }

    // Вспомогательный метод для генерации следующего ID
    private Long generateNextId() {
        return flossContainer.stream()
                .mapToLong(Floss::getId)
                .max()
                .orElse(0L) + 1;
    }

    @Override
    public Floss findByBrandAndNumber(String brand, String number) {
        if (brand == null || number == null) {
            throw new IllegalArgumentException("Brand or number cannot be null");
        }
        return flossContainer.stream()
                .filter(floss -> brand.equals(floss.getBrand()) && number.equals(floss.getColorNumber()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Floss> findByBrand(String brand) {
        if (brand == null) {
            throw new IllegalArgumentException("Brand cannot be null");
        }
        return flossContainer.stream()
                .filter(floss -> brand.equals(floss.getBrand()))
                .toList();
    }

    @Override
    public List<Floss> findByColorGroup(String colorGroup) {
        if (colorGroup == null) {
            throw new IllegalArgumentException("Brand cannot be null");
        }
        return flossContainer.stream()
                .filter(floss -> colorGroup.equals(floss.getBrand()))
                .toList();
    }
}
