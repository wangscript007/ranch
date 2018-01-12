class Generator {
    public random(length: number): string {
        let str = '';
        while (str.length < length) {
            str += Math.random().toString(32).substr(2);
        }

        return str.substring(0, length);
    }
}

export default new Generator();