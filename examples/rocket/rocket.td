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
variable number HeroLife                        ' N� de vidas
variable number HeroY                           ' Posici�n Y de la nave
variable number HeroX                           ' Posici�n X de la nave

variable image  LaserFace = "rocket_laser.png"  ' Imagen del l�ser
variable number LaserY                          ' Posici�n Y del l�ser
variable number LaserX                          ' Posici�n X del l�ser
variable number LaserActive                     ' Distinto de 0, si se ha disparado el l�ser
variable number LaserExpl                       ' Distinto de 0, si el l�ser ha explotado

variable image  RockFace = "rocket_asteroid.png"  ' Imagen de los asteroides
variable number Rocks = 4                         ' N� de asteroides
variable number RockX[Rocks]                      ' Posici�n X de los asteroides
variable number RockY[Rocks]                      ' Posici�n Y de los asteroides

variable image  ExplFace = "rocket_fire.png"    ' Imagen de la explosi�n

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
print center "�Cu�nto tiempo podr�s"
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

' Esperar la pulsaci�n de la tecla Intro
'
do
	set KeyPressed = key()
loop while KeyPressed != "enter"

' Inicializaci�n

random BoardWidth - 1

' Comienzo de una partida

do
	' Inicializaci�n de la partida

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
			call CheckExpl            ' Comprobar si hay colisi�n
			
			if HeroLife == 0 then break

			call RockUpdate           ' Mover un asteroide
			call CheckExpl            ' Comprobar si hay colisi�n
			
			if HeroLife == 0 then break

			' Siguiente asteroide

			set Rock = Rock + 1
		loop while Rock < Rocks

		' Mover el l�ser, si est� activo

		if LaserActive > 0 then
			call LaserUpdate      ' Mover el l�ser
			call CheckExpl        ' Comprobar si hay colisi�n
		end if

		' Una peque�a pausa, para que no vaya
		' todo demasiado r�pido

		wait 100

		' Borrar la explosi�n del l�ser, si est� activa

		if LaserExpl > 0 then
			image LaserX*CellSide, LaserY*CellSide, BoardFace
			set LaserExpl = 0
		end if

	loop while HeroLife > 0

	' Fin de la partida

	dialog YesNo "�Quieres jugar otra partida?"
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

' Actualizar la posici�n de un asteroide
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

' Posici�n inicial de la nave
'
HeroSetPos:
	set HeroY = BoardHeight - 1
	set HeroX = BoardWidth / 2
	image HeroX*CellSide, HeroY*CellSide, HeroFace
	return

' Actualizar la posici�n de la nave
'
HeroUpdate:
	set KeyPressed = key()

	if KeyPressed != "" then

		' Borrar la nave

		image HeroX*CellSide, HeroY*CellSide, BoardFace

		' �Izquierda?

		if KeyPressed == "left" && HeroX > 0 then
			set HeroX = HeroX - 1
		end if

		' �Derecha?
		if KeyPressed == "right" && HeroX < BoardWidth - 1 then
			set HeroX = HeroX + 1
		end if

		' �Disparo?
		if KeyPressed == " " && LaserActive == 0 then
			set LaserX = HeroX
			set LaserY = HeroY
			set LaserActive = 1
		end if

		' Dibujar la nave

		image HeroX*CellSide, HeroY*CellSide, HeroFace
	end if

	return

' Actualizar l�ser
'
LaserUpdate:

	' Borrar l�ser
	
	if LaserY != HeroY then
		image LaserX*CellSide, LaserY*CellSide, BoardFace
	end if
	
	' Modificar la posici�n del l�ser y dibujarlo, o desactivarlo
	' si est� en la parte superior de la pantalla
	
	if LaserY > 0 then
		set LaserY = LaserY - 1
		image LaserX*CellSide, LaserY*CellSide, LaserFace
	else
		set LaserActive = 0
	end if
	return

' Comprobar si hay colisiones entre nuestra nave y los asteroides,
' y entre los asteroides y el l�ser.
'
CheckExpl:
	set n = 0
	do
		' Comprobar l�ser

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

			' Hay colisi�n con un asteroide

			set HeroLife = 0
			image HeroX*CellSide, HeroY*CellSide, ExplFace
			
			' Salir del bucle, fin del juego
			break
		end if

		' Siguiente asteroide

		set n = n + 1
	loop while n < Rocks
	return
