import * as names from 'names.js';

function main() {
    startApplication("run");
    mouseClick(waitForObject(names.ampStartServerButton), 92, 15, 0, Button.Button1);
    mouseClick(waitForObject(names.ampCheckHealthButton, 63447), 75, 12, 0, Button.Button1);
    mouseClick(waitForObject(names.ampCheckHealthButton), 75, 10, 0, Button.Button1);
    mouseClick(waitForObject(names.ampStopServerButton), 74, 10, 0, Button.Button1);
    type(waitForObject(names.ampPortTextField), "31513");
    mouseClick(waitForObject(names.ampStartServerButton), 73, 10, 0, Button.Button1);
    mouseClick(waitForObject(names.ampCheckHealthButton, 55387), 59, 13, 0, Button.Button1);
    mouseClick(waitForObject(names.ampCheckHealthButton), 61, 14, 0, Button.Button1);
    mouseClick(waitForObject(names.ampStopServerButton), 60, 14, 0, Button.Button1);
    mouseClick(waitForObject(names.ampPortTextField), 55, 15, 0, Button.Button1);
    mouseDrag(waitForObject(names.ampPortTextField), 62, 13, -135, 0, Modifier.None, Button.Button1);
    type(waitForObject(names.ampPortTextField), "8181");
}
