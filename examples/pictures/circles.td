' ----------------
' TinyDev: Circles
' ----------------

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
variable number PosX        ' X circle position
variable number PosY        ' Y circle position
variable number Diameter    ' Circle diameter
variable number Iteration   ' Iteration number

' Inicializar pantalla
'
screen 400, 400

' Dibujar 10 círculos
'
set Iteration = 0

do
	call DrawCircle
	set Iteration = Iteration + 1
loop until Iteration == 10

' Fin
'
end

' Dibujar un círculo sólido
'
' La variable Iteration ha de tener un valor de 0 a 9.
'
DrawCircle:
	' Calcular la posición del círculo y su color
	'
	set PosX = 20 * Iteration
	set PosY = PosX
	set Diameter = 400 - Iteration * 40

	' Fijar color
	'
	if Iteration == 0 then pen blue
	else if Iteration == 1 then pen cyan
	else if Iteration == 2 then pen gray
	else if Iteration == 3 then pen red
	else if Iteration == 4 then pen magenta
	else if Iteration == 5 then pen pink
	else if Iteration == 6 then pen orange
	else if Iteration == 7 then pen yellow
	else if Iteration == 8 then pen green
	else if Iteration == 9 then pen black

	' Dibujar el círculo
	'
	ellipse solid PosX, PosY, Diameter, Diameter
	return
