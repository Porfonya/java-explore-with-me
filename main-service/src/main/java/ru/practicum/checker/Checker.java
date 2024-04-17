package ru.practicum.checker;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.categories.CategoriesRepository;
import ru.practicum.categories.model.Category;
import ru.practicum.compilations.Compilation;
import ru.practicum.compilations.CompilationRepository;
import ru.practicum.events.EventRepository;
import ru.practicum.events.model.Event;
import ru.practicum.exception.NotFoundException;
import ru.practicum.requests.RequestRepository;
import ru.practicum.users.UserRepository;
import ru.practicum.users.model.User;

@Component
@AllArgsConstructor
public class Checker {
    private final CategoriesRepository categoriesRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final CompilationRepository compilationRepository;

    public void checkerCategory(Long categoryId) {
        categoriesRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(categoryId));
    }

    public void checkerEvent(Long eventId) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(eventId));
    }


    public void checkerUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(userId));
    }

    public User checkerAndReturnUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(userId));
    }

    public Category checkerAndReturnCategory(Long categoryId) {
        return categoriesRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(categoryId));
    }

    public Event checkerAndReturnEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(eventId));
    }

    public void checkerRequest(Long requestsId) {
        requestRepository.findById(requestsId)
                .orElseThrow(() -> new NotFoundException(requestsId));
    }

    public void checkerCompilation(Long compId) {
        compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(compId));
    }

    public Compilation checkerAndReturnCompilation(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(compId));
    }
}
