-- Datos de prueba para la tabla nutricionista
INSERT INTO nutricionista (nombre, apellido, numero_licencia, especialidad, email, telefono, activo) 
VALUES ('Ana', 'García', 'NUT-2023-001', 'Nutrición Deportiva', 'ana.garcia@ejemplo.com', '555-1234', true);

INSERT INTO nutricionista (nombre, apellido, numero_licencia, especialidad, email, telefono, activo) 
VALUES ('Carlos', 'Rodríguez', 'NUT-2023-002', 'Nutrición Clínica', 'carlos.rodriguez@ejemplo.com', '555-2345', true);

-- Pacientes para la nutricionista Ana García (ID 1)
INSERT INTO paciente (nombre, apellido, fecha_nacimiento, email, telefono, activo, nutricionista_id)
VALUES ('Juan', 'Pérez', '1985-05-15', 'juan.perez@ejemplo.com', '555-1001', true, 1);

INSERT INTO paciente (nombre, apellido, fecha_nacimiento, email, telefono, activo, nutricionista_id)
VALUES ('Laura', 'Martínez', '1990-07-22', 'laura.martinez@ejemplo.com', '555-1002', true, 1);

-- Notas para el paciente Juan Pérez (ID 1)
INSERT INTO nota (titulo, contenido, fecha_creacion, tipo_nota, paciente_id, nutricionista_id)
VALUES ('Evaluación inicial', 'Paciente con sobrepeso leve. IMC: 27.5', '2023-01-15T10:30:00', 'Evaluación', 1, 1);

INSERT INTO nota (titulo, contenido, fecha_creacion, tipo_nota, paciente_id, nutricionista_id)
VALUES ('Plan alimenticio', 'Dieta hipocalórica con restricción moderada de carbohidratos', '2023-01-15T11:00:00', 'Plan', 1, 1);

-- Mediciones para Juan Pérez (ID 1)
INSERT INTO medicion (fecha, peso, altura, circunferencia_cintura, circunferencia_cadera, porcentaje_grasa_corporal, paciente_id, nutricionista_id)
VALUES ('2023-01-15', 85.5, 176.0, 98.0, 102.0, 24.5, 1, 1);

INSERT INTO medicion (fecha, peso, altura, circunferencia_cintura, circunferencia_cadera, porcentaje_grasa_corporal, paciente_id, nutricionista_id)
VALUES ('2023-02-15', 83.0, 176.0, 96.5, 101.0, 23.8, 1, 1);