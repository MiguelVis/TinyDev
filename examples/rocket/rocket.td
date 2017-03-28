' ---------------
' TinyDev: Rocket
' ---------------

' Colores
'
variable string black = "black"
variable string blue = "blue"
variable string cyan = "cyan"
variable string light_gray = "light_gray"
variable string gray = "gray"
variable string dark_gray = "dark_gray"
variable string magenta = "magenta"
variable string orange = "orange"
variable string pink = "pink"
variable string red = "red"
variable string white = "white"
variable string yellow = "yellow"
variable string green = "green"

' Variables
'
variable number BoardHeight = 18                ' Alto del tablero en celdas
variable number BoardWidth  = 12                ' Ancho del tablero en celdas
variable image  BoardFace   = "rocket_sky.png"  ' Imagen del tablero (el espacio)

variable number CellSide = 32                   ' Longitud de las caras de las celdas en pixels

variable image  HeroFace = "rocket_hero.png"    ' Imagen de la nave
variable number HeroLife                        ' Nº de vidas
variable number HeroY                           ' Posición Y de la nave
variable number HeroX                           ' Posición X de la nave

variable image  LaserFace = "rocket_laser.png"  ' Imagen del láser
variable number LaserY                          ' Posición Y del láser
variable number LaserX                          ' Posición X del láser
variable number LaserActive                     ' Distinto de 0, si se ha disparado el láser
variable number LaserExpl                       ' Distinto de 0, si el láser ha explotado

variable image  RockFace = "rocket_asteroid.png"  ' Imagen de los asteroides
variable number Rocks = 4                         ' Nº de asteroides
variable number RockX[Rocks]                      ' Posición X de los asteroides
variable number RockY[Rocks]                      ' Posición Y de los asteroides

variable image  ExplFace = "rocket_fire.png"    ' Imagen de la explosión

variable number Rock                            ' Variable utilizada en algunas subrutinas

variable string KeyPressed                      ' Tecla pulsada

variable number n                               ' Variable utilizada en algunos bucles

' Inicio y presentacion

screen BoardWidth*CellSide, BoardHeight*CellSide
paper black
clear
pen red
font 24 bold
print break()
print center "** ROCKET **"
print break() + break()
pen blue
font 16
print center "¿Cuánto tiempo podrás"
print break()
print center "pilotar tu nave espacial,"
print break()
print center "sin chocar contra un asteroide?"
print break() + break()
print center "Teclado:"
print break() + break()
print center "[<-]  [Espacio]  [->]"
print break() + break()
print center "Pulsa [Intro] para comenzar"

' Esperar la pulsación de la tecla Intro
'
do
	set KeyPressed = key()
loop while KeyPressed != "enter"

' Inicialización

random BoardWidth - 1

' Comienzo de una partida

do
	' Inicialización de la partida

	set HeroLife = 1
	set LaserActive = 0
	set LaserExpl = 0
	clear

	' Posicionar la nave

	call HeroSetPos

	' Posicionar los asteroides

	set Rock = 0
	do
		call RockSetPos
		set Rock = Rock + 1
	loop while Rock < Rocks

	' Bucle principal

	do
		' Mover la nave y los asteroides, comprobando
		' si hay colisiones, en cada paso

		set Rock = 0
		do
			call HeroUpdate           ' Mover nuestra nave
			call CheckExpl            ' Comprobar si hay colisión
			
			if HeroLife == 0 then break

			call RockUpdate           ' Mover un asteroide
			call CheckExpl            ' Comprobar si hay colisión
			
			if HeroLife == 0 then break

			' Siguiente asteroide

			set Rock = Rock + 1
		loop while Rock < Rocks

		' Mover el láser, si está activo

		if LaserActive > 0 then
			call LaserUpdate      ' Mover el láser
			call CheckExpl        ' Comprobar si hay colisión
		end if

		' Una pequeña pausa, para que no vaya
		' todo demasiado rápido

		wait 100

		' Borrar la explosión del láser, si está activa

		if LaserExpl > 0 then
			image LaserX*CellSide, LaserY*CellSide, BoardFace
			set LaserExpl = 0
		end if

	loop while HeroLife > 0

	' Fin de la partida

	dialog YesNo "¿Quieres jugar otra partida?"
loop while reply() == "yes"

' Fin del juego

pen red
font 24, bold
print break() + break() + break()
print center "** GAME OVER **"
end

' Posicionar aleatoriamente un asteroide
'
' Entrada: Rock
'
RockSetPos:
	set RockY[Rock] = Rock
	set RockX[Rock] = random()
	image RockX[Rock]*CellSide, RockY[Rock]*CellSide, RockFace
	return

' Actualizar la posición de un asteroide
'
' Entrada: Rock
'
RockUpdate:
	image RockX[Rock]*CellSide, RockY[Rock]*CellSide, BoardFace
	if RockY[Rock] < BoardHeight - 1 then
		set RockY[Rock] = RockY[Rock] + 1
		image RockX[Rock]*CellSide, RockY[Rock]*CellSide, RockFace
	else
		call RockSetPos
	end if
	return

' Posición inicial de la nave
'
HeroSetPos:
	set HeroY = BoardHeight - 1
	set HeroX = BoardWidth / 2
	image HeroX*CellSide, HeroY*CellSide, HeroFace
	return

' Actualizar la posición de la nave
'
HeroUpdate:
	set KeyPressed = key()

	if KeyPressed != "" then

		' Borrar la nave

		image HeroX*CellSide, HeroY*CellSide, BoardFace

		' ¿Izquierda?

		if KeyPressed == "left" && HeroX > 0 then
			set HeroX = HeroX - 1
		end if

		' ¿Derecha?
		if KeyPressed == "right" && HeroX < BoardWidth - 1 then
			set HeroX = HeroX + 1
		end if

		' ¿Disparo?
		if KeyPressed == " " && LaserActive == 0 then
			set LaserX = HeroX
			set LaserY = HeroY
			set LaserActive = 1
		end if

		' Dibujar la nave

		image HeroX*CellSide, HeroY*CellSide, HeroFace
	end if

	return

' Actualizar láser
'
LaserUpdate:

	' Borrar láser
	
	if LaserY != HeroY then
		image LaserX*CellSide, LaserY*CellSide, BoardFace
	end if
	
	' Modificar la posición del láser y dibujarlo, o desactivarlo
	' si está en la parte superior de la pantalla
	
	if LaserY > 0 then
		set LaserY = LaserY - 1
		image LaserX*CellSide, LaserY*CellSide, LaserFace
	else
		set LaserActive = 0
	end if
	return

' Comprobar si hay colisiones entre nuestra nave y los asteroides,
' y entre los asteroides y el láser.
'
CheckExpl:
	set n = 0
	do
		' Comprobar láser

		if LaserActive > 0 then
			if LaserY == RockY[n] && LaserX == RockX[n] then
				image LaserX*CellSide, LaserY*CellSide, ExplFace
				set Rock = n
				call RockSetPos
				set LaserActive = 0
				set LaserExpl = 1
			end if
		end if

		' Comprobar nuestra nave

		if HeroY == RockY[n] && HeroX == RockX[n] then

			' Hay colisión con un asteroide

			set HeroLife = 0
			image HeroX*CellSide, HeroY*CellSide, ExplFace
			
			' Salir del bucle, fin del juego
			break
		end if

		' Siguiente asteroide

		set n = n + 1
	loop while n < Rocks
	return
