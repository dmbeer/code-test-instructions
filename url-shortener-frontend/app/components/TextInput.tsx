interface TextInputProps {
    value: string;
    onChange: (value: string) => void;
    placeholder?: string;
    type?: string;
    required?: boolean;
    disabled?: boolean;
    label?: string;
    error?: string;
    className?: string;
}

export function TextInput({
    value,
    onChange,
    placeholder,
    type = "text",
    required = false,
    disabled = false,
    label,
    error,
    className = "",
}: TextInputProps) {
    return (
        <div className="flex flex-col gap-1 w-full">
            {label && (
                <label className="text-sm font-medium text-gray-300">
                    {label}
                    {required && <span className="text-red-500 ml-1">*</span>}
                </label>
            )}
            <input
                type={type}
                placeholder={placeholder}
                value={value}
                onChange={(e) => onChange(e.target.value)}
                required={required}
                disabled={disabled}
                className={[
                    "w-full rounded-lg bg-gray-900 border px-4 py-2 text-white placeholder-gray-500",
                    "focus:outline-none focus:ring-2 focus:ring-blue-500 transition",
                    "disabled:opacity-50 disabled:cursor-not-allowed",
                    error ? "border-red-500 focus:ring-red-500" : "border-gray-600",
                    className,
                ]
                    .filter(Boolean)
                    .join(" ")}
            />
            {error && <p className="text-xs text-red-400">{error}</p>}
        </div>
    );
}
