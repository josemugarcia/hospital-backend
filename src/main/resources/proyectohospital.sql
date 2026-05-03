-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 04-06-2024 a las 13:49:12
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `proyectohospital`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `citas`
--

CREATE TABLE `citas` (
  `id_cita` int(11) NOT NULL,
  `fecha` datetime(6) NOT NULL,
  `hora` time NOT NULL,
  `especialidad_id` int(11) NOT NULL,
  `medico_id` int(11) NOT NULL,
  `usuario_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `citas`
--

INSERT INTO `citas` (`id_cita`, `fecha`, `hora`, `especialidad_id`, `medico_id`, `usuario_id`) VALUES
(1, '2024-05-29 19:30:00.000000', '19:30:00', 6, 25, 15),
(2, '2024-05-30 15:00:00.000000', '15:00:00', 5, 6, 15),
(53, '2024-06-05 14:00:00.000000', '14:00:00', 6, 15, 38),
(54, '2024-06-11 12:00:00.000000', '12:00:00', 5, 6, 38),
(58, '2024-07-02 16:00:00.000000', '16:00:00', 1, 1, 47);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `especialidad`
--

CREATE TABLE `especialidad` (
  `id` int(11) NOT NULL,
  `imagen_especialidad` varchar(255) DEFAULT NULL,
  `nombre_especialidad` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `especialidad`
--

INSERT INTO `especialidad` (`id`, `imagen_especialidad`, `nombre_especialidad`) VALUES
(1, 'alergologia.png', 'Alergologia'),
(2, 'cardiologia.png', 'Cardiologia'),
(3, 'dermatologia.png', 'Dermatologia'),
(4, 'fisioterapia.png', 'Fisioterapia'),
(5, 'podologia.png', 'Podologia'),
(6, 'enfermeria.png', 'Enfermeria'),
(7, 'traumatologia.png', 'Traumatologia'),
(8, 'pediatria.png', 'Pediatria');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `medicos`
--

CREATE TABLE `medicos` (
  `id_medico` int(11) NOT NULL,
  `edad_medico` int(11) DEFAULT NULL,
  `imagen_medico` varchar(255) DEFAULT NULL,
  `nombre_medico` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `telefono_medico` int(11) DEFAULT NULL,
  `especialidad_fk` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `medicos`
--

INSERT INTO `medicos` (`id_medico`, `edad_medico`, `imagen_medico`, `nombre_medico`, `status`, `telefono_medico`, `especialidad_fk`) VALUES
(1, 42, 'miriam.png', 'Miriam López', 'true', 754312768, 1),
(3, 30, 'judit.png', 'Judit Rey', 'true', 654321873, 2),
(5, 32, 'antoni.png', 'Antoni Palazon', 'true', 684321654, 4),
(6, 35, 'ismael.png', 'Ismael Segui', 'true', 754321765, 5),
(8, 31, 'marta.png', 'Marta Ferreiro', 'true', 687639865, 7),
(10, 35, 'gisela.png', 'Gisela Caparros', 'true', 654322323, 4),
(12, 44, 'eugenio.png', 'Eugenio Arnaiz', 'true', 687639865, 7),
(13, 38, 'pascual.png', 'Pascual Millan', 'true', 687639823, 4),
(14, 33, 'arantxa.png', 'Arantxa Rosales', 'true', 765432187, 4),
(15, 38, 'camilo.png', 'Camilo Lucena', 'true', 732156438, 6),
(18, 50, 'camilo.png', 'Camila Borrego', 'true', 654331764, 2),
(25, 56, 'meredith.jpg', 'Meredith Grey', 'true', 754312336, 6),
(30, 34, 'aurora.png', 'Aurora Linares', 'true', 654321665, 5);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `role`
--

CREATE TABLE `role` (
  `role_name` varchar(255) NOT NULL,
  `role_description` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `role`
--

INSERT INTO `role` (`role_name`, `role_description`) VALUES
('Admin', 'Admin role'),
('User', 'Default role for newly created record');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `user`
--

CREATE TABLE `user` (
  `user_name` varchar(255) NOT NULL,
  `user_first_name` varchar(255) DEFAULT NULL,
  `user_last_name` varchar(255) DEFAULT NULL,
  `user_password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `user`
--

INSERT INTO `user` (`user_name`, `user_first_name`, `user_last_name`, `user_password`) VALUES
('admin123', 'admin', 'admin', '$2a$10$Y6AILkfvKNiyWHu24TpVle.z/Gd.1n.ppeUgjwQnMeyhe9s1iWttK');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `estado` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `rol` varchar(255) DEFAULT NULL,
  `telefono` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `users`
--

INSERT INTO `users` (`id`, `email`, `estado`, `nombre`, `password`, `rol`, `telefono`) VALUES
(1, 'admin@gmail.com', 'true', 'admin', 'admin', 'admin', '765432176'),
(3, 'herminia@gmail.com', 'true', 'herminia', 'usuario', 'user', '654321768'),
(15, 'laura@gmail.com', 'true', 'laura', 'usuario1234', 'user', '823456788'),
(17, 'carla@gmail.com', 'true', 'Carla', 'usuario', 'user', '712345678'),
(18, 'adri@gmail.com', 'true', 'Adrian', 'usu', 'user', '643232154'),
(19, 'marcelo@gmail.com', 'true', 'Marcelo', '123', 'user', '675432165'),
(20, 'jesus@gmail.com', 'true', 'jesús', 'usuario', 'user', '765476542'),
(26, 'mamen@gmail.com', 'true', 'Mamen', 'usuario', 'user', '654321675'),
(28, 'miguel@gmail.com', 'true', 'Miguel', '12345678', 'user', '654321654'),
(31, 'paco@gmail.com', 'true', 'paco', '12345678', 'user', '654327865'),
(32, 'loli@gmail.com', 'true', 'loli', '12345678', 'user', '765432987'),
(34, 'mari@gmail.com', 'true', 'mari', 'usuario', 'user', '765432875'),
(35, 'paula@gmail.com', 'true', 'paula', 'usuario', 'user', '743216543'),
(38, 'susana.uber-jimenez@iesruizgijon.com', 'true', 'susana', 'usuario', 'user', '654321765'),
(43, 'juanma@gmail.com', 'true', 'juanma', 'usuario', 'user', '765432167'),
(47, 'joselito.3417@gmail.com', 'true', 'jose', 'usuario', 'user', '654321568');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `user_role`
--

CREATE TABLE `user_role` (
  `user_id` varchar(255) NOT NULL,
  `role_id` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `user_role`
--

INSERT INTO `user_role` (`user_id`, `role_id`) VALUES
('admin123', 'Admin');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `citas`
--
ALTER TABLE `citas`
  ADD PRIMARY KEY (`id_cita`),
  ADD KEY `FK7v7rsbt060yqarv39dbu0enb0` (`especialidad_id`),
  ADD KEY `FKbfl57ey8hx1a3ubtrpy8x9ll` (`medico_id`),
  ADD KEY `FKc0sx6xpgbe9dh5ro9vdaplfao` (`usuario_id`);

--
-- Indices de la tabla `especialidad`
--
ALTER TABLE `especialidad`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `medicos`
--
ALTER TABLE `medicos`
  ADD PRIMARY KEY (`id_medico`),
  ADD KEY `FKqpc3xn97gss57tmrmis0vwb8b` (`especialidad_fk`);

--
-- Indices de la tabla `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`role_name`);

--
-- Indices de la tabla `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_name`);

--
-- Indices de la tabla `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `user_role`
--
ALTER TABLE `user_role`
  ADD PRIMARY KEY (`user_id`,`role_id`),
  ADD KEY `FKa68196081fvovjhkek5m97n3y` (`role_id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `citas`
--
ALTER TABLE `citas`
  MODIFY `id_cita` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=59;

--
-- AUTO_INCREMENT de la tabla `especialidad`
--
ALTER TABLE `especialidad`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `medicos`
--
ALTER TABLE `medicos`
  MODIFY `id_medico` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT de la tabla `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=48;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `citas`
--
ALTER TABLE `citas`
  ADD CONSTRAINT `FK7v7rsbt060yqarv39dbu0enb0` FOREIGN KEY (`especialidad_id`) REFERENCES `especialidad` (`id`),
  ADD CONSTRAINT `FKbfl57ey8hx1a3ubtrpy8x9ll` FOREIGN KEY (`medico_id`) REFERENCES `medicos` (`id_medico`),
  ADD CONSTRAINT `FKc0sx6xpgbe9dh5ro9vdaplfao` FOREIGN KEY (`usuario_id`) REFERENCES `users` (`id`);

--
-- Filtros para la tabla `medicos`
--
ALTER TABLE `medicos`
  ADD CONSTRAINT `FKqpc3xn97gss57tmrmis0vwb8b` FOREIGN KEY (`especialidad_fk`) REFERENCES `especialidad` (`id`);

--
-- Filtros para la tabla `user_role`
--
ALTER TABLE `user_role`
  ADD CONSTRAINT `FK859n2jvi8ivhui0rl0esws6o` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_name`),
  ADD CONSTRAINT `FKa68196081fvovjhkek5m97n3y` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_name`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
