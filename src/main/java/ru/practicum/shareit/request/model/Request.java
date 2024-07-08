package ru.practicum.shareit.request.model;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.model.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
@EqualsAndHashCode
@Getter
@Setter
@Entity
@Table(name = "requests", schema = "public")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(name = "description")
    String description;
    @ManyToOne
    @JoinColumn(name = "user_id")
    User requester;
    @Column(name = "created")
    LocalDateTime created;
}
