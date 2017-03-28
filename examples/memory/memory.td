' ---------------
' TinyDev: Memory
' ---------------

' El nº de casillas del tablero ha de ser un número par,
' y como máximo el nº indicado abajo.
'
variable number maximo = 22

variable number tablero_ancho = 5
variable number tablero_alto  = 4

variable number pieza
variable number pieza_lado   = 96
variable image  pieza_imagen = "memory.png"
variable string pieza_color[maximo]
variable number pieza_ver[maximo]

variable number contador

variable string boton

variable number x
variable number y

variable number p1
variable number p2
variable string s1
variable string s2

' Comprobar que el nº máximo de casillas es correcto
'
if tablero_ancho * tablero_alto > maximo then
	dialog error "Las dimensiones del tablero no son correctas."
	end
end if

' Inicializar pantalla
'
screen tablero_ancho*pieza_lado, tablero_alto*pieza_lado

' Jugar partida
'
do
	call Inicializa
	call DibujaTablero

	do
		' Elegir una pieza

		call ElijePieza
		set p1 = pieza
		set pieza_ver[p1] = 1
		call DibujaPieza

		' Elegir otra pieza

		call ElijePieza
		set p2 = pieza
		set pieza_ver[p2] = 1
		call DibujaPieza

		' Comprobar si las 2 piezas son iguales

		if pieza_color[p1] == pieza_color[p2] then
			set contador = contador + 2
		else
			wait 500

			set pieza_ver[p1] = 0
			set pieza = p1
			call DibujaPieza

			set pieza_ver[p2] = 0
			set pieza = p2
			call DibujaPieza
		end if
	loop until contador == tablero_ancho * tablero_alto

	' Fin de la partida: preguntar si quiere jugar otra

	dialog yesNo "¿Quieres jugar otra partida?"
loop while reply() == "yes"

' Fin del juego
'
end

' Dibuja el tablero
'
DibujaTablero:
	set pieza = 0
	do
		call DibujaPieza
		set pieza = pieza + 1
	loop until pieza == tablero_ancho * tablero_alto
	return

' Dibuja una pieza
'
' Entrada: pieza
'
DibujaPieza:
	set x = pieza % tablero_ancho * pieza_lado
	set y = pieza / tablero_ancho * pieza_lado

	image x, y, pieza_imagen

	if pieza_ver[pieza] != 0 then
		pen pieza_color[pieza]
		rectangle solid x+3, y+3, pieza_lado-6, pieza_lado-6
	end if

	return

' Elije una pieza que no esté descubierta
'
' Salida: pieza
'
ElijePieza:
	do
		do
			set boton = mouseClick()
			set x = mouseX()
			set y = mouseY()
		loop until boton == "left" && x != -1 && y != -1

		set x = x / pieza_lado
		set y = y / pieza_lado

		set pieza = y * tablero_ancho + x
	loop until pieza_ver[pieza] == 0

	return

' Inicializar partida
'
Inicializa:
	' Contador de piezas descubiertas

	set contador = 0

	' Cubrir todas las piezas

	set pieza = 0
	do
		set pieza_ver[pieza] = 0
		set pieza = pieza + 1
	loop until pieza == tablero_ancho * tablero_alto

	' Asignar colores a las piezas (sólo 2 piezas
	' con el mismo color)

	set pieza_color[0] = "yellow"
	set pieza_color[1] = "yellow"
	set pieza_color[2] = "blue"
	set pieza_color[3] = "blue"
	set pieza_color[4] = "cyan"
	set pieza_color[5] = "cyan"
	set pieza_color[6] = "gray"
	set pieza_color[7] = "gray"
	set pieza_color[8] = "magenta"
	set pieza_color[9] = "magenta"
	set pieza_color[10] = "orange"
	set pieza_color[11] = "orange"
	set pieza_color[12] = "green"
	set pieza_color[13] = "green"
	set pieza_color[14] = "red"
	set pieza_color[15] = "red"
	set pieza_color[16] = "white"
	set pieza_color[17] = "white"
	set pieza_color[18] = "pink"
	set pieza_color[19] = "pink"
	set pieza_color[20] = "black"
	set pieza_color[21] = "black"

	' Mezclar las piezas

	random (tablero_ancho * tablero_alto) - 1

	set pieza = 0
	do
		' Tomar 2 piezas

		set p1 = random()
		set p2 = random()

		' Tomar sus colores

		set s1 = pieza_color[p1]
		set s2 = pieza_color[p2]

		' Intercambiar los colores

		set pieza_color[p1] = s2
		set pieza_color[p2] = s1

		' Otra más

		set pieza = pieza + 1
	loop until pieza == 32               ' <- Cuantas más parejas de piezas mezclemos, mejor

	return
