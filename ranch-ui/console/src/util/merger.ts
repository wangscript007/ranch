class Merger {
    public merge<T extends object>(target: T, ...sources: T[]): T {
        for (const source of sources) {
            for (const key in source) {
                if (source.hasOwnProperty(key)) {
                    target[key] = source[key];
                }
            }
        }

        return target;
    }
}

export default new Merger();