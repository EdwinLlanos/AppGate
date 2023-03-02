# appgate

## Prueba técnica Android Software Engineer

###  Las dos prácticas que considero tienen un gran impacto al momento de asegurar la calidad del código son:
1. Pruebas automatizadas: El objetivo principal de escribir pruebas unitarias, funcionales o de integración es verificar el correcto funcionamiento de manera automática, con lo que se puede tener un mayor control sobre los cambios, considerando que cualquier afectación al correcto funcionamiento será notificado por una prueba rota.
2. Revisión exhaustiva del código: El momento de integrar nuevo código al repositorio a través de los __Pull Request__, es una gran oportunidad para que varios integrantes del equipo logren identificar fallos en la solución propuesta, de igual manera detalles sencillos que pueden afectar en gran medida la calidad del código.

Estas dos prácticas anteriores pueden ser aseguradas con la correcta definición de sus respectivos procesos: Un ejemplo de esto sería: si se implementa una arquitectura como __CLEAN__,
para la revisión de código podría ser una opción entregar tres __Pull Request__, donde en el primero esté la solución a la capa de datos con sus respetivas pruebas unitarias, en el segundo __PR__ la capa de dominio con sus pruebas unitarias, y el tercer __PR__ la capa de presentación con sus pruebas funcionales. Este tipo de procesos ayudan tanto a la revisión del código, como en la correcta implementación de las pruebas.
