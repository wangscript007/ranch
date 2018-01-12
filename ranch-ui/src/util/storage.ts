class Storage {
    public title(title?: string): string {
        return this.data('ranch-ui.title', title) || 'Ranch UI';
    }

    public data(key: string, value?: any): any {
        if (value)
            localStorage[key] = value;

        return localStorage[key];
    }
}

export default new Storage();