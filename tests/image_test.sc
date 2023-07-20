using import String
using import itertools
using import Array
using import UTF-8

import ..noise
import .stdio

fn write-image (name w h data)
    let file = (stdio.fopen name "wb")
    assert (file != null)

    stdio.fprintf file "P5 %d %d 255 " w h
    ptr count := 'data data
    stdio.fwrite ptr 1 count file
    stdio.fclose file

fn luminosity (v)
    let vrange = 2.0
    normalized := (v + (vrange / 2)) / vrange
    (normalized * 255) as u8

let img-width img-height zdepth wdepth = 600 600 5 5
let ox oy oz ow = 300:f64 300:f64 400:f64 400:f64
let sx sy sz sw = .01:f64 .01:f64 .1:f64 .1:f64
global generator : noise.OpenSimplex2S 0

inline test2D (name noisef)
    local values : (Array u8)
    'reserve values (img-width * img-height)

    for x y in (dim img-width img-height)
        x as:= f64
        y as:= f64
        let v = (noisef generator ((x + ox) * sx) ((y + oy) * sy))
        'append values (luminosity v)
    write-image name img-width img-height values

inline test3D (name noisef)
    local values : (Array u8)
    'reserve values (img-width * img-height * zdepth)

    for x y z in (dim img-width img-height zdepth)
        x as:= f64
        y as:= f64
        z as:= f64
        let v = (noisef generator ((x + ox) * sx) ((y + oy) * sy) ((z + oz) * sz))
        'append values (luminosity v)
    write-image name img-width (img-height * zdepth) values

inline test4D (name noisef)
    local values : (Array u8)
    'reserve values (img-width * img-height * zdepth)

    for x w y z in (dim img-width wdepth img-height zdepth)
        x as:= f64
        y as:= f64
        z as:= f64
        w as:= f64
        let v = (noisef generator ((x + ox) * sx) ((y + oy) * sy) ((z + oz) * sz) ((w + ow) * sw))
        'append values (luminosity v)
    write-image name (img-width * wdepth) (img-height * zdepth) values

fn main (argc argv)
    let OS2 = noise.OpenSimplex2S

    test2D S"noise2.pgm" OS2.noise2
    test2D S"noise2-XbeforeY.pgm" OS2.noise2-XbeforeY

    test3D S"noise3-classic.pgm" OS2.noise3-classic
    test3D S"noise3-XYbeforeZ.pgm" OS2.noise3-XYbeforeZ
    test3D S"noise3-XZbeforeY.pgm" OS2.noise3-XZbeforeY

    test4D S"noise4-classic.pgm" OS2.noise4-classic
    test4D S"noise4-XYbeforeZW.pgm" OS2.noise4-XYbeforeZW
    test4D S"noise4-XZbeforeYW.pgm" OS2.noise4-XZbeforeYW
    test4D S"noise4-XYZbeforeW.pgm" OS2.noise4-XYZbeforeW

    0

RUN? := false

using import compiler.target.C
# hook-compile-function;
run-stage;

static-if RUN?
    main 0 0
else
    compile-object
        default-target-triple
        compiler-file-kind-object
        "test.o"
        do
            let main = (static-typify main i32 (mutable@ rawstring))
            locals;
        'O3
