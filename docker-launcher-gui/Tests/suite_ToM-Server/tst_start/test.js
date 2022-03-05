import * as names from 'names.js';

function main() {
    startApplication("run");
    mouseClick(waitForObject(names.ampStartServerButton), 43, 10, 0, Button.Button1);
    mouseClick(waitForObject(names.ampCheckHealthButton, 59143), 76, 5, 0, Button.Button1);
    mouseClick(waitForObject(names.ampCheckHealthButton), 76, 13, 0, Button.Button1);
    mouseClick(waitForObject(names.ampCheckHealthButton), 76, 13, 0, Button.Button1);
}
