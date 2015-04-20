--- server.lua - RESTFull Server for Rover remot control
-- Author: Stefano Viola <stefanoviola85@gmail.com>
-- Hackathon 2015 Torino
-- 
-- API:
--  http://IPADDRESS:8888/api/direction
--      Move the rover.
--      Method: POST
--      Data: RAW Json
--      {
--          "direction":"up"
--          "speed":1
--      }
--      possible direction: up|down|left|right
--      possible speed: 1-5
--
-- http://IPADDRESS:8888/api/speed
--      Return speed.
--      Method: GET
--      
-- http://IPADDRESS:8888/api/break
--      Brake the rover.
--      Method: POST
--      Data: RAW Json
--      {
--          "intensity":10
--      }
--


-- Import turbo,
local turbo = require("turbo")

-- Create a new requesthandler with a method get() for HTTP GET.
local commander = {}
commander.speed = "1"
commander.brake = "./commander.sh /dev/ttyACM0 0 6000"
commander.forward = "./commander.sh /dev/ttyACM0 0 6500"
commander.back = "./commander.sh /dev/ttyACM0 0 5500"
commander.left = "./commander.sh /dev/ttyACM0 1 10000"
commander.right = "./commander.sh /dev/ttyACM0 1 1000"
commander.reset = "./commander.sh /dev/ttyACM0 1 8350"


local SpeedHandler = class("SpeedHandler", turbo.web.RequestHandler)
local BrakeHandler = class("BrakeHandler", turbo.web.RequestHandler)
local DirectionHandler = class("DirectionHandler", turbo.web.RequestHandler)
local WebHandler = class("WebHandler", turbo.web.RequestHandler)

function SpeedHandler:get()
    self:write({speed=commander.speed})
end

function forward(speed)
    if speed == 1 then
        print("Received SPEED: 1")
        commander.speed = commander.speed
        io.popen("./commander.sh /dev/ttyACM0 0 6500")
    elseif speed == 2 then
        print("Received SPEED: 2")
        commander.speed = commander.speed
        io.popen("./commander.sh /dev/ttyACM0 0 6700")
    elseif speed == 3 then
        print("Received SPEED: 3")
        commander.speed = commander.speed
        io.popen("./commander.sh /dev/ttyACM0 0 6800")
    elseif speed == 4 then
        print("Received SPEED: 4")
        commander.speed = commander.speed
        io.popen("./commander.sh /dev/ttyACM0 0 6900")
    elseif speed == 5 then
        print("Received SPEED: 5")
        commander.speed = commander.speed
        io.popen("./commander.sh /dev/ttyACM0 0 7000")
    end
    
end

function BrakeHandler:post()
    local data = self:get_json(true)
    local intensity = data.intensity
    print("intensity: " .. intensity)
    io.popen(commander.brake .. " && " .. commander.reset)
    -- local result = handle:read("*a")
    -- io.popen(commander.reset)
    self:write({speed=commander.speed})
end

function DirectionHandler:post()
    local data = self:get_json(true)
    print("direction: " .. data.direction)
    print("speed: " .. data.speed)
    commander.speed = data.speed

    if data.direction == "up" then
        print("Received UP command...")
        io.popen(commander.forward)
        forward(data.speed)
    elseif data.direction == "down" then
        print("Received DOWN command...")
        io.popen(commander.back)
    elseif data.direction == "left" then
        print("Received LEFT command...")
        io.popen(commander.left)
    elseif data.direction == "right" then
        print("Received RIGHT command...")
        io.popen(commander.right)
    elseif data.direction == "reset" then
        print("Received RESET command...")
        io.popen(commander.reset)	
    end
    self:write({speed=commander.speed})
end

function WebHandler:get()
    self:write("WEB INTERFACE")
end

-- Create an Application object and bind our HelloWorldHandler to the route '/hello'.
local app = turbo.web.Application:new({
    {"/api/speed", SpeedHandler},
    {"/api/direction", DirectionHandler},
    {"/api/brake", BrakeHandler}
})

-- Create an Application object and bind our WEB to the route '/web'.
local appWeb = turbo.web.Application:new({
    {"/web", WebHandler}
})
-- Set the server to listen on port 8888 and start the ioloop.
app:listen(8888)
appWeb:listen(8899)
turbo.ioloop.instance():start()
