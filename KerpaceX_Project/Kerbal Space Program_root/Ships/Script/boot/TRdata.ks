switch to 0.
set w to latlng(0,0).
until false{
    if addons:tr:hasimpact=0 {
        set w to ship:geoposition.
    }
    else {
        set w to addons:tr:impactpos.
    }
    print w.
    writeJson(w,"TRdata.json").
    wait 0.1.
}