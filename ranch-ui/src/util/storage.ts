class Storage {
    public data(key: string, value?: any): any {
        if (value)
            localStorage[key] = value;

        return localStorage[key];
    }
}

export default new Storage();