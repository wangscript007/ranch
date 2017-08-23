window.meta.put("ranch.weixin", {
    "key": "ranch.weixin",
    "props": [{
            "name": "key"
        },
        {
            "name": "name"
        },
        {
            "name": "appId"
        },
        {
            "name": "secret"
        },
        {
            "name": "token"
        },
        {
            "name": "mchId"
        },
        {
            "name": "mchKey"
        },
        {
            "name": "accessToken"
        },
        {
            "name": "jsapiTicket"
        },
        {
            "name": "time"
        }
    ],
    "query": {
        "page": "grid",
        "ops": [{
                "type": "modify"
            },
            {
                "type": "delete",
                "service": "delete",
                "success": "query"
            }
        ],
        "toolbar": [{
            "type": "create"
        }]
    },
    "create": {
        "page": "form",
        "toolbar": [{
            "type": "save",
            "service": "save",
            "success": "query",
            "data": "true"
        }]
    },
    "modify": {
        "page": "form",
        "toolbar": [{
            "type": "save",
            "service": "save",
            "success": "query",
            "data": "true"
        }]
    }
});