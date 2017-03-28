' ---------------------------
' TinyDev : Builing (picture)
' ---------------------------

' Colores
'
variable string black = "black"
variable string blue = "blue"
variable string cyan = "cyan"
variable string gray = "gray"
variable string magenta = "magenta"
variable string orange = "orange"
variable string pink = "pink"
variable string red = "red"
variable string white = "white"
variable string yellow = "yellow"
variable string green = "green"

' Variables
'
variable number PosX
variable number PosY

variable number Item

' Inicializar pantalla
'
screen 450, 450

paper cyan
clear

' Título
'
pen black
font bold
print center "Un edificio"

' El edificio
'
paper orange
rectangle fill 50, 50, 225, 350

' La puerta
'
paper black
rectangle fill 137, 325, 50, 75

' Las ventanas
'
set PosX = 75
set PosY = 75
call Windows

set PosX = 125
set PosY = 75
call Windows

set PosX = 175
set PosY = 75
call Windows

set PosX = 225
set PosY = 75
call Windows

' El árbol
'
paper orange
rectangle fill 360, 250, 15, 150

paper green
ellipse fill 316, 200, 100, 100

paper red
ellipse fill 350, 225, 10, 10
ellipse fill 390, 260, 10, 10
ellipse fill 355, 250, 10, 10
ellipse fill 330, 275, 10, 10

' El sol
'
paper yellow
ellipse fill 360, 20, 80, 80

' El suelo
'
paper gray
rectangle fill 0, 399, 450, 51

' Fin
'
end

' Dibujar ventanas
'
Windows:
	paper blue
	set Item = 0
	do
		rectangle fill PosX, PosY, 25, 50
		set PosY = PosY + 75
		set Item = Item + 1
	loop while Item < 3
	return
