switch to 0.
set p to ship:parts[0].
set w to latlng(0,0).
until false{
    if addons:tr:hasimpact=0 {
        set w to ship:geoposition.
    }
    else {
        set w to addons:tr:impactpos.
    }
    set p:tag to w:lat:tostring+","+w:lng:tostring.
    wait 0.05.
}