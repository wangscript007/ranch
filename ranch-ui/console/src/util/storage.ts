class Storage {
    public data(key: string, value?: any): any {
        if (value) {
            localStorage[key] = value;
        }

        return localStorage[key];
    }

    public remove(key: string): void {
        localStorage.removeItem(key);
    }
}

export default new Storage();