var tableIndex = 0;
var pcIndex = 0;
var portIndex = 0;
var rackIndex = 0;
var patchIndex = 0;
var slotIndex = 0;

function setPC() {
  if(tableIndex && pcIndex && portIndex)
  {
    $("#ports .slot").removeClass("active");
    $("#ports .rack").removeClass("active");
    rackIndex = 0;
    patchIndex = 0;
    slotIndex = 0;
    if(portIndex == 6 || portIndex == 12)
    {
      $("#ports #info").text("RESERVED");
      return;
    }
    if(portIndex == 5 || portIndex == 11)
    {
      $("#ports #info").text("NETWORK (TUO)");
      return;
    }
    $("#ports #info").text("CONNECTED");
    var port = portIndex;
    var rack = 1;
    var patch = 1;
    var table = tableIndex;
    if(table > 2)
    {
      table -= 2;
      rack += 2;
    }
    if(portIndex == 3 || portIndex == 4)
    {
      port -= 2;
      rack++;
    }
    else if(portIndex == 7 || portIndex == 8)
    {
      port -= 4;
    }
    else if(portIndex == 9 || portIndex == 10)
    {
      port -= 6;
      rack++;
    }
    port += (pcIndex - 1)*4;
    port += (table - 1)*16;
    if(port > 24)
    {
      port -= 24;
      patch++;
    }
    rackIndex = rack;
    patchIndex = patch;
    slotIndex = port;
    $("#ports .patch[data-patch=\"" + patch + "\"] .slot[data-slot=" + port + "]").addClass("active");
    $("#ports .rack[data-rack=" + rack + "]").addClass("active");
  }
}

function setRack() {
  if(rackIndex && patchIndex && slotIndex)
  {
  	$("#ports .pc").removeClass("active");
    $("#ports .port").removeClass("active");
    tableIndex = 0;
    pcIndex = 0;
    portIndex = 0;
  	if(patchIndex == 2 && slotIndex > 8)
  	{
		if(slotIndex > 22)
		{
			$("#ports #info").text("RESERVED");
		}
		else if(slotIndex > 10)
		{
			$("#ports #info").text("TO RDX");
		}
		else
		{
			portIndex = (slotIndex - 8) + (rackIndex-1)*2;
			if(rackIndex > 2)
			{
				portIndex += 2;
			}
			$("#ports #info").text("TEACHER PC " + portIndex);
		}
		return;
	}
	if(patchIndex == 3)
  	{
		if(slotIndex > 9)
		{
			$("#ports #info").text("RESERVED");
		}
		else
		{
  			$("#ports #info").text(slotIndex > 5 ? "ROUTER CONSOLE " + (slotIndex - 5) + " (R" + String.fromCharCode(64+((slotIndex - 5) + (rackIndex-1)*4)) + ")" : "SWITCH CONSOLE " + slotIndex);
		}
		return;
	}
  	$("#ports #info").text("CONNECTED");
  	var port = slotIndex;
  	var table = 1;
  	var pc = 1;
  	if(patchIndex == 2)
  	{
			port += 24;
		}
		if(rackIndex == 3 || rackIndex == 4)
		{
			table += 2;
		}
		pc = Math.floor((port-1) / 4) + 1;
		if(pc > 4)
		{
			pc -= 4;
			table++;
		}
		port = ((port-1) % 4) + 1;
		if(port > 2)
		{
			port += 4;
		}
		if(rackIndex == 2 || rackIndex == 4)
		{
			port += 2;
		}
		tableIndex = table;
    pcIndex = pc;
    portIndex = port;
    $("#ports .table[data-table=\"" + table + "\"] .pc[data-pc=\"" + pc + "\"]").addClass("active");
    $("#ports .port[data-port=" + port + "]").addClass("active");
  }
}

$(document).ready(function(){

  $("#ports .patch").each(function(index, element){
    for(var i = 1; i <= 24; i++)
    {
      var slot = $("<div class=\"slot\" data-slot=\"" + i + "\">" + i + "</div>");
      slot.click(function(){
        $("#ports .slot").removeClass("active");
        $(this).addClass("active");
        patchIndex = $(this).parent().data("patch");
        slotIndex = $(this).data("slot");
        setRack();
      });
      $(element).append(slot);
    }
    $(element).append($("<hr />"));
  });

  $("#ports .pc").click(function(){
    $("#ports .pc").removeClass("active");
    $(this).addClass("active");
    tableIndex = $(this).parent().data("table");
    pcIndex = $(this).data("pc");
    setPC();
  });
  $("#ports .port").click(function(){
    $("#ports .port").removeClass("active");
    $(this).addClass("active");
    portIndex = $(this).data("port");
    setPC();
  });
  $("#ports .rack").click(function(){
    $("#ports .rack").removeClass("active");
    $(this).addClass("active");
    rackIndex = $(this).data("rack");
    setRack();
  });

});
