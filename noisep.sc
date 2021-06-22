# precompiled version
sugar-if main-module?
    set-globals!    
        'bind (globals) 'assert
            (...) -> ()
    run-stage;
    using import .noise
    compile-object
        default-target-triple
        compiler-file-kind-object
        "noise.o"
        do
            let 
                OS2noise_new = 
                    static-typify
                        fn (seed)
                            OpenSimplex2S seed
                        u64
                OS2noise2 = (static-typify OpenSimplex2S.noise2 OpenSimplex2S f64 f64)
                OS2noise2XbeforeY = (static-typify OpenSimplex2S.noise2-XbeforeY OpenSimplex2S f64 f64)
                OS2noise3classic = (static-typify OpenSimplex2S.noise3-classic OpenSimplex2S f64 f64 f64)
                OS2noise3XYbeforeZ = (static-typify OpenSimplex2S.noise3-XYbeforeZ OpenSimplex2S f64 f64 f64)
                OS2noise3XZbeforeY = (static-typify OpenSimplex2S.noise3-XZbeforeY OpenSimplex2S f64 f64 f64)
                OS2noise4classic = (static-typify OpenSimplex2S.noise4-classic OpenSimplex2S f64 f64 f64 f64)
                OS2noise4XYbeforeZW = (static-typify OpenSimplex2S.noise4-XYbeforeZW OpenSimplex2S f64 f64 f64 f64)
                OS2noise4XZbeforeYW = (static-typify OpenSimplex2S.noise4-XZbeforeYW OpenSimplex2S f64 f64 f64 f64)
                OS2noise4XYZbeforeW = (static-typify OpenSimplex2S.noise4-XYZbeforeW OpenSimplex2S f64 f64 f64 f64)
            locals;            
        'O2
else
    using import struct
    using import Array

    struct Grad2 plain
        dx : f64
        dy : f64

    struct Grad3 plain
        dx : f64
        dy : f64
        dz : f64

    struct Grad4 plain
        dx : f64
        dy : f64
        dz : f64
        dw : f64

    let PSIZE = 2048
    struct OpenSimplex2S
        _perm : (Array i16 PSIZE)
        _perm-grad2 : (Array Grad2 PSIZE)
        _perm-grad3 : (Array Grad3 PSIZE)
        _perm-grad4 : (Array Grad4 PSIZE)

        inline __typecall (cls seed)
            (extern 'OS2noise_new (function (uniqueof this-type -1) u64)) seed
        let noise2 = (extern 'OS2noise2 (function f64 (viewof this-type) f64 f64))
        let noise2-XbeforeY = (extern 'OS2noise2XbeforeY (function f64 (viewof this-type) f64 f64))
        let noise3-classic = (extern 'OS2noise3classic (function f64 (viewof this-type) f64 f64 f64))
        let noise3-XYbeforeZ = (extern 'OS2noise3XYbeforeZ (function f64 (viewof this-type) f64 f64 f64))
        let noise3-XZbeforeY = (extern 'OS2noise3XZbeforeY (function f64 (viewof this-type) f64 f64 f64))
        let noise4-classic = (extern 'OS2noise4classic (function f64 (viewof this-type) f64 f64 f64 f64))
        let noise4-XYbeforeZW = (extern 'OS2noise4XYbeforeZW (function f64 (viewof this-type) f64 f64 f64 f64))
        let noise4-XZbeforeYW = (extern 'OS2noise4XZbeforeYW (function f64 (viewof this-type) f64 f64 f64 f64))
        let noise4-XYZbeforeW = (extern 'OS2noise4XYZbeforeW (function f64 (viewof this-type) f64 f64  f64  f64))

    do
        let OpenSimplex2S 
        locals;
