<#compress>
${apiPrefix} = { 
	"url": "${apiResponse.url}"
	,"type":"remoting"
	<#assign namespace = "${apiResponse.nameSpace}" >
	<#if namespace != "">
		,"namespace": ${namespace}
	</#if>
	,"actions": {
		<#list apiResponse.actions as action>
			"${action.name}":[
				<#list action.methods as method>
					{
						"name":"${method.name}"
						,"len":${method.len}
					}<#if method_has_next>,</#if>
				</#list>
			]
			<#if action_has_next>,</#if>
		</#list>
	}
};
</#compress>