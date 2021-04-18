using import String
using import itertools
using import Array
using import UTF-8

import ..noise
import .stdio

fn... write-image (name : rawstring, w, h, data)
    let file = (stdio.fopen name "wb")
    assert (file != null)

    stdio.fprintf file ("P5 %d %d 255 " as rawstring) w h
    stdio.fwrite ((imply data pointer) as (mutable@ void)) 1 (countof data) file
    stdio.fclose file

fn main (argc argv)
    let gen = (noise.OpenSimplex2S 0)
    let w h = 600 600
    let ox oy oz ow = 300:f64 300:f64 0:f64 0:f64
    let scale = 100:f64

    local values : (Array u8)

    fn luminosity (v)
        let vrange = 2.0
        normalized := (v + (vrange / 2)) / vrange
        (normalized * 255) as u8

    for x y in (dim w h)
        x as:= f64
        y as:= f64
        let v = ('noise2-XbeforeY gen ((x + ox) / scale) ((y + oy) / scale))
        'append values (luminosity v)

    write-image "2dnoise.pgm" w h values

    'clear values
    let zdepth = 5
    for x y z in (dim w h zdepth)
        x as:= f64
        y as:= f64
        z as:= f64
        let v = ('noise3-XYbeforeZ gen ((x + ox) / scale) ((y + oy) / scale) (((z * 10) + oz) / scale))
        'append values (luminosity v)
    write-image "3dnoise.pgm" w (h * zdepth) values

    'clear values
    let wdepth = 5
    for x w y z in (dim w wdepth h zdepth)
        x as:= f64
        y as:= f64
        z as:= f64
        w as:= f64
        let v = ('noise4-XYZbeforeW gen ((x + ox) / scale) ((y + oy) / scale) (((z * 10) + oz) / scale) (((w * 10) + ow) / scale))
        'append values (luminosity v)
    write-image "4dnoise.pgm" (w * wdepth) (h * zdepth) values

    0

main 0 0

# compile-object
#     default-target-triple
#     compiler-file-kind-object
#     "test.o"
#     do
#         let main = (static-typify main i32 (mutable@ rawstring))
#         locals;
