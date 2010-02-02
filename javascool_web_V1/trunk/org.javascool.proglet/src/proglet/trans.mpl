# Rotation of axis u, |u| = 1, angle a, after a translation t
Rt := (u, a, t) -> linalg[stackmatrix](linalg[augment](Student[LinearAlgebra][RotationMatrix](a, u), t), Vector[row]([0, 0, 0, 1])):
# Axes and memchanism
e_x := Vector([1, 0, 0]): e_y := Vector([0, 1, 0]): e_z := Vector([0, 0, 1]): z := Vector([0, 0, 0]):
# Transformation et projection
tilt := 0: pitch := 0:
P := Rt(e_z, pan, z) &* Rt(e_x, tilt, z) &* Vector([X, Y, Z, 1]):
Pl := evalm(Rt(e_z, gaze - vergence, z) &* Rt(e_x, pitch, z) &* Rt(e_y, 0, Vector([base/2, 0, elevation])) &* P);
Pr := evalm(Rt(e_z, gaze + vergence, z) &* Rt(e_x, pitch, z) &* Rt(e_y, 0, Vector([-base/2, 0, elevation])) &* P);
Pl := combine(Pl); Pr := combine(Pr);
CodeGeneration[Java]([Pl0 = Pl[1], Pl1 = Pl[2], Pl2 = Pl[3], Pr0 = Pr[1], Pr1 = Pr[2], Pr2 = Pr[3]]);




