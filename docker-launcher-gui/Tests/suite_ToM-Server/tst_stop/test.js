import * as names from 'names.js';

function main() {
    startApplication("run");
    mouseClick(waitForObject(names.ampCheckHealthButton), 66, 11, 0, Button.Button1);
    mouseClick(waitForObject(names.ampCheckHealthButton), 81, 15, 0, Button.Button1);
    mouseClick(waitForObject(names.ampStopServerButton), 81, 16, 0, Button.Button1);
    type(waitForObject(names.ampPortTextField), "<Right>");
    type(waitForObject(names.ampPortTextField), "<Backspace>");
    type(waitForObject(names.ampPortTextField), "2");
    mouseClick(waitForObject(names.ampGridPane), 357, 6, 0, Button.Button1);
}
