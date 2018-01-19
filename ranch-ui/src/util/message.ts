class Message {
    private messages: string[] = [];
    private lang: string[] = [];

    constructor() {
        let language: string = (navigator.language || "zh-cn").toLowerCase();
        this.lang.push('.' + language);
        this.lang.push('.' + language.split('-')[0]);
    }

    public put(object: object): void {
        for (let key in object) {
            let obj = object[key];
            if (typeof obj === 'string')
                this.messages[key] = obj;
            else if (typeof obj === 'object')
                for (let k in obj)
                    this.messages[key + '.' + k] = obj[k];
        }
    }

    public get(key: string): string {
        return this.messages[key + this.lang[0]] || this.messages[key + this.lang[1]] || this.messages[key] || key;
    }
}

export default new Message();