' -------------------------
' TinyDev : Truck (picture)
' -------------------------

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

' Inicializar pantalla
'
screen 450, 350
paper cyan
clear

' Título
'
pen black
font bold
print break()
print center "Un bonito camión"

paper gray
rectangle fill 50, 200, 350, 100

paper green
rectangle fill 150, 150, 250, 100

' La ventana
'
paper blue
rectangle fill 60, 210, 80, 40

' Una rueda
'
paper black
ellipse fill 80, 270, 60, 60

paper gray
ellipse fill 102, 292, 15, 15

' Otra rueda
'
paper black
ellipse fill 300, 270, 60, 60

paper gray
ellipse fill 322, 292, 15, 15
