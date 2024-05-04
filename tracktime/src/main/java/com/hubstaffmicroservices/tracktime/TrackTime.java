package com.hubstaffmicroservices.tracktime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity

//@RedisHash("TrackTime")
//@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackTime implements Serializable {


    @Id
    @GeneratedValue
    private Integer id;


    private Integer userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @Column(
            insertable = false,
            updatable = true
    )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;


    public void updateAttributeNames(Map<String, String> attributeMappings) {
        try {
            for (Map.Entry<String, String> entry : attributeMappings.entrySet()) {
                String oldAttributeName = entry.getKey();
                String newAttributeName = entry.getValue();

                Field oldField = this.getClass().getDeclaredField(oldAttributeName);
                oldField.setAccessible(true); // Make the old field accessible

                // Check if the new field exists
                Field newField = null;
                try {
                    newField = this.getClass().getDeclaredField(newAttributeName);
                } catch (NoSuchFieldException e) {
                    // If the new field doesn't exist, create it
                    newField = createField(newAttributeName, oldField.getType());
                }

                newField.setAccessible(true); // Make the new field accessible

                // Get the value of the old field
                Object value = oldField.get(this);

                // Set the value to the new field
                newField.set(this, value);

                // Remove the old field
                oldField.set(this, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to create a new field
    private Field createField(String fieldName, Class<?> type) throws NoSuchFieldException, IllegalAccessException {
        try {
            Field newField = Field.class.getDeclaredField("name");
            newField.setAccessible(true);
            newField.set(newField, fieldName);

            Field typeField = Field.class.getDeclaredField("type");
            typeField.setAccessible(true);
            typeField.set(newField, type);

            return newField;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public String toString() {
        return "TrackTime{" +
                "id=" + id +
                ", userId=" + userId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}



